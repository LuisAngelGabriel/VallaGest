package edu.ucne.vallagest.presentation.ordenes

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    totalVallas: Double,
    onBack: () -> Unit,
    onConfirmarMetodo: (Double, Int) -> Unit,
    viewModel: OrdenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val totalFinal = totalVallas * state.meses

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Checkout") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp).fillMaxSize()) {
            Text("Resumen", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Card(Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("Meses:", Modifier.weight(1f))
                    IconButton(onClick = { if (state.meses > 1) viewModel.onEvent(OrdenUiEvent.OnMesesChange(state.meses - 1)) }) { Icon(Icons.Default.Remove, null) }
                    Text("${state.meses}", style = MaterialTheme.typography.headlineSmall)
                    IconButton(onClick = { viewModel.onEvent(OrdenUiEvent.OnMesesChange(state.meses + 1)) }) { Icon(Icons.Default.Add, null) }
                }
            }
            Text("Método de Pago")
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = state.metodoPago == 0, onClick = { viewModel.onEvent(OrdenUiEvent.OnMetodoChange(0)) })
                    Text("Tarjeta de Crédito/Debito")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = state.metodoPago == 1, onClick = { viewModel.onEvent(OrdenUiEvent.OnMetodoChange(1)) })
                    Text("Transferencia Bancaria")
                }
            }
            Spacer(Modifier.weight(1f))
            Button(onClick = { onConfirmarMetodo(totalFinal, state.metodoPago) }, modifier = Modifier.fillMaxWidth().height(56.dp)) {
                Text("CONTINUAR A RD$ $totalFinal")
            }
        }
    }
}