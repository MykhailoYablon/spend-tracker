package com.example.spendtracker.composable.calculator.bond

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendtracker.composable.calculator.common.SplitInputField
import com.example.spendtracker.ds.entity.CalculationResult
import com.example.spendtracker.model.CalculationViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.math.pow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculationContent(
    calculationViewModel: CalculationViewModel
) {
    var purchasePriceText by remember { mutableStateOf("1000") }
    var couponRateText by remember { mutableStateOf("17") }
    var bankReturnText by remember { mutableStateOf("1170") }
    var selectedDate by remember { mutableStateOf<LocalDate?>(LocalDate.now().plusYears(1)) }
    var showDatePicker by remember { mutableStateOf(false) }
    var resultText by remember { mutableStateOf<String?>(null) }

    val datePickerState = rememberDatePickerState()

    fun millisToLocalDate(millis: Long?): LocalDate? {
        if (millis == null) return null
        return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    fun calculate() {
        val price = purchasePriceText.replace(',', '.').toDoubleOrNull()
        val couponRate = couponRateText.replace(',', '.').toDoubleOrNull()
        val bankReturn = bankReturnText.replace(',', '.').toDoubleOrNull()
        val returnDate = selectedDate
        if (price == null || couponRate == null || returnDate == null || bankReturn == null) {
            resultText = "Please enter valid price, yield, bank return, and date."
            return
        }
        val today = LocalDate.now()
        val days = ChronoUnit.DAYS.between(today, returnDate).coerceAtLeast(0)
        val years = days / 365.00
        val theoreticalFutureValue = price * (1.0 + (couponRate / 100.0)).pow(years)
        val realReturn = bankReturn - price
        var bankCommission = 0.0
        if (theoreticalFutureValue - bankReturn > 0) {
            bankCommission = theoreticalFutureValue - bankReturn
        } else bankCommission = 0.0
        val realPercentage = if (years > 0.0) {
            (((bankReturn / price).pow(1.0 / years) - 1.0) * 100.0)
        } else {
            ((realReturn / price) * 100.0)
        }
        resultText = buildString {
            append(
                String.format(
                    Locale.getDefault(),
                    "Theoretical Future Value: %.2f\n",
                    theoreticalFutureValue
                )
            )
            append(String.format(Locale.getDefault(), "Bank return: %.2f\n", bankReturn))
            append(String.format(Locale.getDefault(), "Real return: %.2f\n", realReturn))
            append(String.format(Locale.getDefault(), "Bank commission: %.2f\n", bankCommission))
            append(
                String.format(
                    Locale.getDefault(),
                    "Real %% (with commission): %.2f%%",
                    realPercentage
                )
            )
        }

        // Save to database
        val calculationResult = CalculationResult(
            theoreticalFutureValue = theoreticalFutureValue,
            bankReturn = bankReturn,
            realReturn = realReturn,
            bankCommission = bankCommission,
            returnDate = returnDate,
            realPercentage = realPercentage
        )
        calculationViewModel.add(calculationResult)
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
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Input Fields
        SplitInputField(
            label = "Purchase Price ($)",
            value = purchasePriceText,
            onValueChange = { purchasePriceText = it },
            keyboardType = KeyboardType.Number
        )

        SplitInputField(
            label = "Bank Return ($)",
            value = bankReturnText,
            onValueChange = { bankReturnText = it },
            keyboardType = KeyboardType.Number
        )

        SplitInputField(
            label = "Coupon Rate (%)",
            value = couponRateText,
            onValueChange = { couponRateText = it },
            keyboardType = KeyboardType.Number
        )

        // Date picker row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedDate?.toString() ?: "Select return date",
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = { showDatePicker = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4A90E2)
                )
            ) {
                Text("Pick Date", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Calculate Button
        Button(
            onClick = { calculate() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4A90E2)
            )
        ) {
            Text(
                text = "CALCULATE RETURN",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        // Results
        resultText?.let { text ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF2A2A2A)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier.padding(20.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}