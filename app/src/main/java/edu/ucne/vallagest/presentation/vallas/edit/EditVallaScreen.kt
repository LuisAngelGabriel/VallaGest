package edu.ucne.vallagest.presentation.vallas.edit

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditVallaScreen(
    vallaId: Int,
    isAdmin: Boolean,
    goBack: () -> Unit,
    viewModel: EditVallaViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var expanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val isReadOnly = !isAdmin && vallaId != 0
    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.onEvent(EditVallaUiEvent.ImagenSeleccionada(it.toString()), context)
        }
    }

    LaunchedEffect(vallaId) {
        viewModel.onEvent(EditVallaUiEvent.Load(vallaId))
    }

    LaunchedEffect(state.saved) {
        if (state.saved) goBack()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (isAdmin) "Gestionar Valla" else "Detalles de Valla") },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .padding(vertical = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (state.imagenUrl.isNullOrEmpty()) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
                )
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    if (!state.imagenUrl.isNullOrEmpty()) {
                        AsyncImage(
                            model = state.imagenUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                Icons.Default.PhotoCamera,
                                null,
                                Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Formatos sugeridos: JPG, PNG",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    if (!isReadOnly) {
                        Surface(
                            onClick = { galleryLauncher.launch("image/*") },
                            color = Color.Black.copy(alpha = 0.5f),
                            shape = CircleShape,
                            modifier = Modifier
                                .size(50.dp)
                                .align(Alignment.BottomEnd)
                                .padding(8.dp)
                        ) {
                            Icon(Icons.Default.Edit, null, Modifier.padding(12.dp), tint = Color.White)
                        }
                    }
                }
            }

            if (isReadOnly) {
                InfoItem("Título", state.nombre)
                InfoItem("Categoría", state.categorias.find { it.categoriaId == state.categoriaId }?.nombre ?: "Sin Categoría")
                InfoItem("Precio Mensual", "RD$ ${state.precioMensual}")
                InfoItem("Ubicación", state.ubicacion)
                InfoItem("Descripción", state.descripcion)
            } else {
                Text("Información General", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp))

                OutlinedTextField(
                    value = state.nombre,
                    onValueChange = { viewModel.onEvent(EditVallaUiEvent.NombreChanged(it)) },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.nombreError != null,
                    supportingText = { state.nombreError?.let { Text(it) } }
                )
                Spacer(Modifier.height(12.dp))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = state.categorias.find { it.categoriaId == state.categoriaId }?.nombre ?: "Seleccionar Categoría",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoría") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true).fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        state.categorias.forEach { categoria ->
                            DropdownMenuItem(
                                text = { Text(categoria.nombre) },
                                onClick = {
                                    viewModel.onEvent(EditVallaUiEvent.CategoriaChanged(categoria.categoriaId))
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = state.precioMensual,
                    onValueChange = { viewModel.onEvent(EditVallaUiEvent.PrecioChanged(it)) },
                    label = { Text("Precio Mensual RD$") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.precioError != null,
                    supportingText = { state.precioError?.let { Text(it) } }
                )
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = state.ubicacion,
                    onValueChange = { viewModel.onEvent(EditVallaUiEvent.UbicacionChanged(it)) },
                    label = { Text("Ubicación") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.ubicacionError != null,
                    supportingText = { state.ubicacionError?.let { Text(it) } }
                )
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = state.descripcion,
                    onValueChange = { viewModel.onEvent(EditVallaUiEvent.DescripcionChanged(it)) },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth().height(120.dp)
                )

                Button(
                    onClick = { viewModel.onEvent(EditVallaUiEvent.Save) },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp).height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !state.isSaving
                ) {
                    if (state.isSaving) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    else Text("Guardar Cambios", fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Column(Modifier.padding(vertical = 10.dp)) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
        HorizontalDivider(
            Modifier.padding(top = 8.dp),
            color = Color.LightGray.copy(alpha = 0.5f)
        )
    }
}