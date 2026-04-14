package edu.ucne.vallagest.presentation.pago

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.vallagest.presentation.ordenes.OrdenUiEvent
import edu.ucne.vallagest.presentation.ordenes.OrdenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagoTarjetaScreen(
    total: Double,
    meses: Int,
    onBack: () -> Unit,
    onPagoExitoso: () -> Unit,
    viewModel: OrdenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var nombreHabilitado by remember { mutableStateOf("") }
    var numeroTarjeta by remember { mutableStateOf("") }
    var fechaExpiracion by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    val formularioValido = nombreHabilitado.isNotBlank() &&
            numeroTarjeta.length == 16 &&
            fechaExpiracion.length == 4 &&
            cvv.length == 3

    LaunchedEffect(Unit) {
        viewModel.onEvent(OrdenUiEvent.OnMesesChange(meses))
        viewModel.onEvent(OrdenUiEvent.OnMetodoChange(0))
    }

    LaunchedEffect(state.pagoExitoso) {
        if (state.pagoExitoso) {
            Toast.makeText(context, "Pago realizado con éxito", Toast.LENGTH_LONG).show()
            onPagoExitoso()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Pago con Tarjeta", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(24.dp).fillMaxSize()) {
            OutlinedTextField(
                value = nombreHabilitado,
                onValueChange = { nombreHabilitado = it },
                label = { Text("Nombre en la tarjeta") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
            )

            OutlinedTextField(
                value = numeroTarjeta,
                onValueChange = { if (it.length <= 16) numeroTarjeta = it },
                label = { Text("Número de Tarjeta") },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                shape = RoundedCornerShape(12.dp),
                trailingIcon = { Icon(Icons.Default.CreditCard, null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = fechaExpiracion,
                    onValueChange = { if (it.length <= 4) fechaExpiracion = it },
                    label = { Text("MM/AA") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = cvv,
                    onValueChange = { if (it.length <= 3) cvv = it },
                    label = { Text("CVV") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = { Icon(Icons.Default.Lock, null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(Modifier.height(32.dp))
            HorizontalDivider()

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Total a pagar", style = MaterialTheme.typography.titleMedium)
                Text("RD$ ${String.format("%,.2f", total)}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { viewModel.onEvent(OrdenUiEvent.Pagar(total)) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = formularioValido && !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("Confirmar Pago", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
        }
    }
}