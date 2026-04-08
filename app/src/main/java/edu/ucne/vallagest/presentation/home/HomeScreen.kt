package edu.ucne.vallagest.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import edu.ucne.vallagest.R
import edu.ucne.vallagest.domain.vallas.model.Valla

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    goToValla: (Int) -> Unit,
    createValla: () -> Unit,
    goToPerfil: () -> Unit,
    goToCategorias: () -> Unit,
    goToCarrito: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val usuario by viewModel.usuarioLogueado.collectAsStateWithLifecycle()
    val isAdmin = usuario?.rol == "Admin"
    var searchText by remember { mutableStateOf("") }
    var vallaAEliminar by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) {
        viewModel.observeVallas()
        viewModel.observeCarritoCount()
    }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceContainer) {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.Search, null) },
                    label = { Text("Explorar") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = goToCategorias,
                    icon = { Icon(Icons.Default.List, null) },
                    label = { Text("Categorias") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = goToCarrito,
                    icon = {
                        BadgedBox(
                            badge = {
                                if (state.carritoCount > 0) {
                                    Badge {
                                        Text(state.carritoCount.toString())
                                    }
                                }
                            }
                        ) {
                            Icon(Icons.Default.ShoppingCart, null)
                        }
                    },
                    label = { Text("Carrito") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = goToPerfil,
                    icon = { Icon(Icons.Default.Person, null) },
                    label = { Text("Cuenta") }
                )
            }
        },
        floatingActionButton = {
            if (isAdmin) {
                FloatingActionButton(
                    onClick = createValla,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            }
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(span = { GridItemSpan(2) }) {
                Column {
                    Text(
                        text = "Encuentra Tu Valla\nPerfecta",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Buscar ciudad o avenida...") },
                        leadingIcon = { Icon(Icons.Default.Search, null, tint = MaterialTheme.colorScheme.primary) },
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            if (state.isLoading) {
                item(span = { GridItemSpan(2) }) {
                    Box(Modifier.fillMaxWidth().height(200.dp), Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            } else {
                items(state.vallas.filter { it.nombre.contains(searchText, true) }) { valla ->
                    VallaGridItem(
                        valla = valla,
                        isAdmin = isAdmin,
                        onEdit = { goToValla(valla.vallaId) },
                        onDelete = { vallaAEliminar = valla.vallaId },
                        onClick = { goToValla(valla.vallaId) },
                        onAddToCart = {
                            viewModel.onAgregarAlCarrito(valla.vallaId)
                        }
                    )
                }
            }
        }
    }

    vallaAEliminar?.let { id ->
        AlertDialog(
            onDismissRequest = { vallaAEliminar = null },
            title = { Text("Eliminar") },
            text = { Text("¿Deseas eliminar esta valla?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onDelete(id)
                        vallaAEliminar = null
                    }
                ) {
                    Text("ELIMINAR", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { vallaAEliminar = null }) {
                    Text("CANCELAR")
                }
            }
        )
    }
}

@Composable
fun VallaGridItem(
    valla: Valla,
    isAdmin: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            Box(Modifier.height(110.dp).fillMaxWidth()) {
                AsyncImage(
                    model = valla.imagenUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    error = painterResource(R.drawable.valla_placeholder),
                    placeholder = painterResource(R.drawable.valla_placeholder)
                )

                Surface(
                    color = if (valla.estaOcupada) Color(0xFFE57373) else Color(0xFF81C784),
                    shape = RoundedCornerShape(bottomEnd = 8.dp),
                    modifier = Modifier.align(Alignment.BottomStart)
                ) {
                    Text(
                        text = if (valla.estaOcupada) "Ocupada" else "Disponible",
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    onClick = { if (!valla.estaOcupada) onAddToCart() },
                    shape = CircleShape,
                    color = if (valla.estaOcupada) Color.Gray.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.6f),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(6.dp)
                        .size(30.dp)
                ) {
                    Icon(
                        imageVector = if (valla.estaOcupada) Icons.Default.Block else Icons.Default.AddShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.padding(6.dp),
                        tint = Color.White
                    )
                }

                if (isAdmin) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Surface(
                            onClick = onEdit,
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                            modifier = Modifier.size(26.dp)
                        ) {
                            Icon(Icons.Default.Edit, null, Modifier.padding(5.dp), tint = Color.White)
                        }
                        Surface(
                            onClick = onDelete,
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.error.copy(alpha = 0.9f),
                            modifier = Modifier.size(26.dp)
                        ) {
                            Icon(Icons.Default.Delete, null, Modifier.padding(5.dp), tint = Color.White)
                        }
                    }
                }
            }

            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    valla.nombre,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    valla.descripcion ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "RD$ ${valla.precioMensual}/mes",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        null,
                        tint = Color.Gray,
                        modifier = Modifier.size(10.dp)
                    )
                    Text(
                        valla.ubicacion,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray,
                        maxLines = 1
                    )
                }
            }
        }
    }
}