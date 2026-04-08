package edu.ucne.vallagest.presentation.pago

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.vallagest.presentation.ordenes.OrdenUiEvent
import edu.ucne.vallagest.presentation.ordenes.OrdenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagoTransferenciaScreen(
    total: Double,
    meses: Int,
    onBack: () -> Unit,
    onPagoExitoso: () -> Unit,
    viewModel: OrdenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { viewModel.uploadComprobante(it, context) }
    }

    LaunchedEffect(Unit) {
        viewModel.onEvent(OrdenUiEvent.OnMesesChange(meses))
        viewModel.onEvent(OrdenUiEvent.OnMetodoChange(2))
    }

    LaunchedEffect(state.pagoExitoso) {
        if (state.pagoExitoso) onPagoExitoso()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Pago por Transferencia", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                )
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text(
                        "MONTO TOTAL",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "RD$ ${String.format("%,.2f", total)}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Text(
                "Cuentas Bancarias",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                fontWeight = FontWeight.Bold
            )

            val cuentas = listOf(
                CuentaBancaria("Banreservas", "960-245124-1", Color(0xFFE31D2B)),
                CuentaBancaria("Banco Popular", "754-214587-2", Color(0xFF005691))
            )

            cuentas.forEach { cuenta ->
                CardCuenta(cuenta)
            }

            Spacer(Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .border(
                        width = 2.dp,
                        color = if (state.comprobanteUrl != null) Color(0xFF4CAF50) else MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (state.comprobanteUrl != null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF4CAF50), modifier = Modifier.size(56.dp))
                        Spacer(Modifier.height(8.dp))
                        Text("Comprobante Cargado", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
                    }
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.CloudUpload, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(56.dp))
                        Spacer(Modifier.height(8.dp))
                        Text("Subir Comprobante", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = { viewModel.onEvent(OrdenUiEvent.Pagar(total)) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = state.comprobanteUrl != null && !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                } else {
                    Text("Finalizar Alquiler", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
        }
    }
}

data class CuentaBancaria(val nombre: String, val numero: String, val colorLogo: Color)

@Composable
fun CardCuenta(cuenta: CuentaBancaria) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                Modifier
                    .size(44.dp)
                    .background(cuenta.colorLogo, RoundedCornerShape(8.dp))
            )
            Column(
                Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            ) {
                Text(
                    cuenta.nombre,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    "Cta. ${cuenta.numero}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(onClick = {  }) {
                Icon(
                    Icons.Default.ContentCopy,
                    null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}