package edu.ucne.vallagest.presentation.categorias.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.vallagest.domain.categorias.model.Categoria

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriaListScreen(
    onAdd: () -> Unit,
    onEdit: (Int) -> Unit,
    goToExplorar: () -> Unit,
    goToCarrito: () -> Unit,
    goToPerfil: () -> Unit,
    viewModel: CategoriaListViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }
    var categoriaADetalle by remember { mutableStateOf<Categoria?>(null) }
    var categoriaAEliminar by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) {
        viewModel.onEvent(CategoriaListUiEvent.Refresh)
    }

    val filteredCategorias = remember(searchQuery, state.categorias) {
        state.categorias.filter { it.nombre.contains(searchQuery, ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "CATEGORÍAS",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceContainer) {
                NavigationBarItem(
                    selected = false,
                    onClick = goToExplorar,
                    icon = { Icon(Icons.Default.Search, null) },
                    label = { Text("Explorar") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.List, null) },
                    label = { Text("Categorias") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = goToCarrito,
                    icon = { Icon(Icons.Default.ShoppingCart, null) },
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
            if (state.isAdmin) {
                FloatingActionButton(
                    onClick = onAdd,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) { Icon(Icons.Default.Add, null) }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                placeholder = { Text("Buscar categoría...") },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = MaterialTheme.colorScheme.primary) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(filteredCategorias, key = { it.categoriaId }) { categoria ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { categoriaADetalle = categoria },
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = categoria.nombre,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = categoria.descripcion,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1
                                    )
                                }
                                if (state.isAdmin) {
                                    IconButton(onClick = { onEdit(categoria.categoriaId) }) {
                                        Icon(Icons.Default.Edit, null, tint = MaterialTheme.colorScheme.primary)
                                    }
                                    IconButton(onClick = { categoriaAEliminar = categoria.categoriaId }) {
                                        Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    categoriaADetalle?.let { categoria ->
        AlertDialog(
            onDismissRequest = { categoriaADetalle = null },
            title = { Text(categoria.nombre, fontWeight = FontWeight.Bold) },
            text = { Text(categoria.descripcion) },
            confirmButton = { TextButton(onClick = { categoriaADetalle = null }) { Text("CERRAR") } }
        )
    }

    categoriaAEliminar?.let { id ->
        AlertDialog(
            onDismissRequest = { categoriaAEliminar = null },
            title = { Text("Eliminar") },
            text = { Text("¿Deseas eliminar esta categoría?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onEvent(CategoriaListUiEvent.Delete(id))
                    categoriaAEliminar = null
                }) { Text("ELIMINAR", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = { TextButton(onClick = { categoriaAEliminar = null }) { Text("CANCELAR") } }
        )
    }
}