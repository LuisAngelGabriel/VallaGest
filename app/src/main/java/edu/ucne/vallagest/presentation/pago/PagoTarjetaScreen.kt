package edu.ucne.vallagest.presentation.pago

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.vallagest.presentation.ordenes.OrdenUiEvent
import edu.ucne.vallagest.presentation.ordenes.OrdenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagoTarjetaScreen(
    total: Double,
    onBack: () -> Unit,
    onPagoExitoso: () -> Unit,
    viewModel: OrdenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    var nombreHabilitado by remember { mutableStateOf("") }
    var numeroTarjeta by remember { mutableStateOf("") }
    var fechaExpiracion by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    val esNombreValido = nombreHabilitado.isNotBlank()
    val esNumeroValido = numeroTarjeta.length == 16
    val esFechaValida = fechaExpiracion.length == 4
    val esCvvValido = cvv.length == 3
    val formularioValido = esNombreValido && esNumeroValido && esFechaValida && esCvvValido

    LaunchedEffect(state.pagoExitoso) { if (state.pagoExitoso) onPagoExitoso() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Pago con Tarjeta", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFF0F172A))
            )
        },
        containerColor = Color(0xFF0F172A)
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp).fillMaxSize()) {
            Text("Información de la Tarjeta", color = Color.Gray, modifier = Modifier.padding(bottom = 8.dp))

            OutlinedTextField(
                value = nombreHabilitado,
                onValueChange = { nombreHabilitado = it },
                label = { Text("Nombre en la tarjeta") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF8B5CF6),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color(0xFF8B5CF6),
                    cursorColor = Color(0xFF8B5CF6),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            OutlinedTextField(
                value = numeroTarjeta,
                onValueChange = { if (it.length <= 16) numeroTarjeta = it },
                label = { Text("Número de Tarjeta") },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                trailingIcon = { Icon(Icons.Default.CreditCard, null, tint = Color.Gray) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF8B5CF6), unfocusedBorderColor = Color.Gray, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = fechaExpiracion,
                    onValueChange = { if (it.length <= 4) fechaExpiracion = it },
                    label = { Text("MM/AA") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF8B5CF6), unfocusedBorderColor = Color.Gray, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
                OutlinedTextField(
                    value = cvv,
                    onValueChange = { if (it.length <= 3) cvv = it },
                    label = { Text("CVV") },
                    modifier = Modifier.weight(1f),
                    trailingIcon = { Icon(Icons.Default.Lock, null, tint = Color.Gray) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF8B5CF6), unfocusedBorderColor = Color.Gray, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
            }

            Spacer(Modifier.height(24.dp))
            HorizontalDivider(color = Color.Gray)

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Total a pagar", style = MaterialTheme.typography.titleMedium, color = Color.White)
                Text("RD$ ${String.format("%,.2f", total)}", style = MaterialTheme.typography.headlineSmall, color = Color(0xFF8B5CF6), fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { viewModel.onEvent(OrdenUiEvent.Pagar(total)) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6), contentColor = Color.White, disabledContainerColor = Color.Gray),
                enabled = formularioValido && !state.isLoading
            ) {
                if (state.isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                else Text("Confirmar Pago", fontWeight = FontWeight.Bold)
            }
        }
    }
}