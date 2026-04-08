package edu.ucne.vallagest.presentation.misalquileres

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.vallagest.domain.ordenes.model.Orden

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlquileresScreen(
    onNavigateBack: () -> Unit,
    viewModel: AlquileresViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var alquilerAEliminar by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mis Alquileres Activos", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.alquileres.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No tienes vallas activas", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 20.dp)
            ) {
                items(state.alquileres) { orden ->
                    AlquilerTicketItem(
                        orden = orden,
                        onCancelar = { alquilerAEliminar = orden.ordenId }
                    )
                }
            }
        }
    }

    alquilerAEliminar?.let { id ->
        AlertDialog(
            onDismissRequest = { alquilerAEliminar = null },
            title = { Text("Terminar Alquiler") },
            text = { Text("¿Deseas terminar el alquiler de esta valla?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onEvent(AlquileresUiEvent.CancelarAlquiler(id))
                    alquilerAEliminar = null
                }) { Text("TERMINAR", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { alquilerAEliminar = null }) { Text("CANCELAR") }
            }
        )
    }
}

@Composable
fun AlquilerTicketItem(
    orden: Orden,
    onCancelar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.weight(1f)) {
                    orden.detalles.forEach { detalle ->
                        Text(
                            text = detalle.nombreValla,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "${detalle.meses} meses de contrato",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
                IconButton(
                    onClick = onCancelar,
                    modifier = Modifier.clip(CircleShape).background(MaterialTheme.colorScheme.errorContainer).size(36.dp)
                ) {
                    Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("FECHA INICIO", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    Text(text = orden.fechaOrden.split("T")[0], fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("ESTADO", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    Surface(
                        color = if (orden.estado.lowercase() == "completado") Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = orden.estado.uppercase(),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (orden.estado.lowercase() == "completado") Color(0xFF2E7D32) else Color(0xFFD32F2F)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(thickness = 0.5.dp, color = Color.Gray.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("TOTAL PAGADO", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color.Gray)
                Text(
                    text = "RD$ ${String.format("%,.2f", orden.total)}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}