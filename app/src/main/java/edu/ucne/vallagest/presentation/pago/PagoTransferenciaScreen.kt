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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.vallagest.presentation.ordenes.OrdenUiEvent
import edu.ucne.vallagest.presentation.ordenes.OrdenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagoTransferenciaScreen(
    total: Double,
    onBack: () -> Unit,
    onPagoExitoso: () -> Unit,
    viewModel: OrdenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { viewModel.uploadComprobante(it, context) }
    }

    LaunchedEffect(state.pagoExitoso) { if (state.pagoExitoso) onPagoExitoso() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Pago por Transferencia", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White) } },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFF0F172A))
            )
        },
        containerColor = Color(0xFF0F172A)
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Card(Modifier.fillMaxWidth().padding(bottom = 24.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
                Column(Modifier.padding(16.dp)) {
                    Text("MONTO TOTAL", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                    Text("RD$ ${String.format("%,.2f", total)}", style = MaterialTheme.typography.headlineMedium, color = Color(0xFF8B5CF6), fontWeight = FontWeight.Bold)
                }
            }

            Text("Cuentas Bancarias", color = Color.White, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), fontWeight = FontWeight.Bold)

            val cuentas = listOf(
                CuentaBancaria("Banreservas", "960-245124-1", Color(0xFFE31D2B)),
                CuentaBancaria("Banco Popular", "754-214587-2", Color(0xFF005691))
            )

            cuentas.forEach { CardCuenta(it) }

            Spacer(Modifier.height(24.dp))

            Box(
                modifier = Modifier.fillMaxWidth().height(140.dp).border(1.dp, Color.Gray, RoundedCornerShape(8.dp)).background(Color(0xFF1E293B), RoundedCornerShape(8.dp)).clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (state.comprobanteUrl != null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.CheckCircle, null, tint = Color.Green, modifier = Modifier.size(48.dp))
                        Text("Comprobante Listo", color = Color.Green, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.CloudUpload, null, tint = Color(0xFF8B5CF6), modifier = Modifier.size(48.dp))
                        Text("Subir Comprobante", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = { viewModel.onEvent(OrdenUiEvent.Pagar(total)) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6), contentColor = Color.White),
                enabled = state.comprobanteUrl != null && !state.isLoading
            ) {
                if (state.isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                else Text("Finalizar Alquiler", fontWeight = FontWeight.Bold)
            }
        }
    }
}

data class CuentaBancaria(val nombre: String, val numero: String, val colorLogo: Color)

@Composable
fun CardCuenta(cuenta: CuentaBancaria) {
    Card(Modifier.fillMaxWidth().padding(vertical = 4.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(Modifier.size(40.dp).background(cuenta.colorLogo, RoundedCornerShape(4.dp)))
            Column(Modifier.padding(start = 12.dp).weight(1f)) {
                Text(cuenta.nombre, color = Color.White, fontWeight = FontWeight.Bold)
                Text("Cta. ${cuenta.numero}", color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
            }
            Icon(Icons.Default.ContentCopy, null, tint = Color.Gray)
        }
    }
}