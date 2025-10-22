package com.example.spendtracker.composable.calculator.commission

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.spendtracker.composable.calculator.SplitInputField
import com.example.spendtracker.model.CommissionViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.math.pow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommissionContent(
    viewModel: CommissionViewModel
) {
    var valueText by remember { mutableStateOf("1000") }
    var exchange by remember { mutableStateOf("41.1999") }
    var bankCommissions by remember { mutableStateOf("10") }
    var brokerAmount by remember { mutableStateOf("990") }

    var bankCommissionsList by remember { mutableStateOf(listOf("")) }

    var transactionDate by remember { mutableStateOf<LocalDate?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var resultText by remember { mutableStateOf<String?>(null) }



    val datePickerState = rememberDatePickerState()

    fun millisToLocalDate(millis: Long?): LocalDate? {
        if (millis == null) return null
        return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    fun calculate() {
        val price = valueText.replace(',', '.').toDoubleOrNull()
        val couponRate = exchange.replace(',', '.').toDoubleOrNull()
        val bankReturn = bankCommissions.replace(',', '.').toDoubleOrNull()
        val returnDate = transactionDate
        if (price == null || couponRate == null || returnDate == null || bankReturn == null) {
            resultText = "Please enter valid price, yield, bank return, and date."
            return
        }

        // Parse and sum all bank commissions
        val commissions = bankCommissionsList
            .mapNotNull { it.replace(',', '.').toDoubleOrNull() }
        val totalCommissions = commissions.sum()


        bankCommissions = commissions.joinToString(",")


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
//        val calculationResult = CalculationResult(
//            theoreticalFutureValue = theoreticalFutureValue,
//            bankReturn = bankReturn,
//            realReturn = realReturn,
//            bankCommission = bankCommission,
//            returnDate = returnDate,
//            realPercentage = realPercentage
//        )
//        viewModel.add(calculationResult)
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    transactionDate = millisToLocalDate(datePickerState.selectedDateMillis)
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
            label = "Initial Amount ($)",
            value = valueText,
            onValueChange = { valueText = it },
            keyboardType = KeyboardType.Number
        )

        SplitInputField(
            label = "Exchange Rate",
            value = exchange,
            onValueChange = { exchange = it },
            keyboardType = KeyboardType.Number
        )

        // Bank Commissions with add/remove functionality
        Text(
            text = "Bank Commissions ($)",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        bankCommissionsList.forEachIndexed { index, commission ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SplitInputField(
                    label = if (index == 0) "Commission" else "Additional ${index}",
                    value = commission,
                    onValueChange = { newValue ->
                        bankCommissionsList = bankCommissionsList.toMutableList().apply {
                            this[index] = newValue
                        }
                    },
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )

                if (index > 0) {
                    IconButton(
                        onClick = {
                            bankCommissionsList = bankCommissionsList.toMutableList().apply {
                                removeAt(index)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remove commission",
                            tint = Color.Red
                        )
                    }
                }
            }
        }

        OutlinedButton(
            onClick = {
                bankCommissionsList = bankCommissionsList + ""
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color(0xFF3AAB3E)
            ),
            border = BorderStroke(1.dp, Color(0xFF3AAB3E))
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add commission"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Commission")
        }

        SplitInputField(
            label = "Broker account received",
            value = brokerAmount,
            onValueChange = { brokerAmount = it },
            keyboardType = KeyboardType.Number
        )

        // Date picker row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = transactionDate?.toString() ?: "Select transaction date",
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = { showDatePicker = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3AAB3E)
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
                containerColor = Color(0xFF3AAB3E)
            )
        ) {
            Text(
                text = "CALCULATE INVESTMENT",
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