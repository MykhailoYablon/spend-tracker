package com.example.spendtracker.composable.calculator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.math.pow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BondScreen() {
    var priceText by remember { mutableStateOf("") }
    var yieldText by remember { mutableStateOf("") }
    var bankReturnText by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var resultText by remember { mutableStateOf<String?>(null) }

    val datePickerState = rememberDatePickerState()

    fun millisToLocalDate(millis: Long?): LocalDate? {
        if (millis == null) return null
        return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    fun calculate() {
        val price = priceText.replace(',', '.').toDoubleOrNull()
        val yieldPercent = yieldText.replace(',', '.').toDoubleOrNull()
        val bankReturn = bankReturnText.replace(',', '.').toDoubleOrNull()
        val date = selectedDate
        if (price == null || yieldPercent == null || date == null || bankReturn == null) {
            resultText = "Please enter valid price, yield, bank return, and date."
            return
        }
        val today = LocalDate.now()
        val days = ChronoUnit.DAYS.between(today, date).coerceAtLeast(0)
        val years = days / 365.25
        val theoreticalFutureValue = price * (1.0 + (yieldPercent / 100.0)).pow(years)
        val realReturn = bankReturn - price
        val bankCommission = theoreticalFutureValue - bankReturn
        val realPercentage = if (years > 0.0) {
            (((bankReturn / price).pow(1.0 / years) - 1.0) * 100.0)
        } else {
            // If same day, fall back to simple (non-annualized) percentage
            ((realReturn / price) * 100.0)
        }
        resultText = buildString {
            append(String.format(Locale.getDefault(), "Theoretical FV: %.2f\n", theoreticalFutureValue))
            append(String.format(Locale.getDefault(), "Bank return: %.2f\n", bankReturn))
            append(String.format(Locale.getDefault(), "Real return: %.2f\n", realReturn))
            append(String.format(Locale.getDefault(), "Bank commission: %.2f\n", bankCommission))
            // Escape the literal percent before "(with commission)" and at the end
            append(String.format(Locale.getDefault(), "Real %% (with commission): %.2f%%", realPercentage))
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedDate = millisToLocalDate(datePickerState.selectedDateMillis)
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("Cancel") } }
        ) { DatePicker(state = datePickerState) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Bond Return Calculator",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = priceText,
            onValueChange = { priceText = it },
            label = { Text("Bond price (e.g., 1051.24)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = yieldText,
            onValueChange = { yieldText = it },
            label = { Text("Annual yield % (e.g., 17.35)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = bankReturnText,
            onValueChange = { bankReturnText = it },
            label = { Text("Bank return (e.g., 1324)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = selectedDate?.toString() ?: "Select return date",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f),
                color = Color.White
            )
            Button(onClick = { showDatePicker = true }) { Text("Pick Date") }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { calculate() }, modifier = Modifier.fillMaxWidth()) { Text("Calculate") }

        Spacer(modifier = Modifier.height(16.dp))

        resultText?.let { text ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
private fun textFieldColors() = TextFieldDefaults.colors(
    focusedLabelColor = Color.White,
    unfocusedLabelColor = Color.White,
    focusedTextColor = Color.Black,
    unfocusedTextColor = Color.Black,
    cursorColor = Color.Black,
    focusedIndicatorColor = Color.Black,
    unfocusedIndicatorColor = Color.Black
)
