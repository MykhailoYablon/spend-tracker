package com.example.spendtracker.composable.calculator.funds

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
import com.example.spendtracker.ds.entity.FundDepositResult
import com.example.spendtracker.model.FundDepositViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundDepositContent(
    viewModel: FundDepositViewModel
) {
    var valueText by remember { mutableStateOf("1000") }
    var exchange by remember { mutableStateOf("41.1999") }
    var bankCommissions by remember { mutableStateOf("10") }
    var brokerAmount by remember { mutableStateOf("990") }

    var bankCommissionsList by remember { mutableStateOf(listOf("")) }

    var transactionDate by remember { mutableStateOf<LocalDate?>(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var resultText by remember { mutableStateOf<String?>(null) }



    val datePickerState = rememberDatePickerState()

    fun millisToLocalDate(millis: Long?): LocalDate? {
        if (millis == null) return null
        return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    fun calculate() {
        val initialAmount = valueText.replace(',', '.').toDoubleOrNull()
        val exchangeRate = exchange.replace(',', '.').toDoubleOrNull()
        val finalAmount = brokerAmount.replace(',', '.').toDoubleOrNull()
        // Parse and sum all bank commissions
        val commissions = bankCommissionsList
            .mapNotNull { it.replace(',', '.').toDoubleOrNull() }
        val totalCommissions = commissions.sum()

        if (initialAmount == null || exchangeRate == null || transactionDate == null ||
            finalAmount == null || commissions.isEmpty()) {
            resultText = "Please enter valid price, yield, bank return, and date."
            return
        }

        resultText = buildString {
            append(
                String.format(
                    Locale.getDefault(),
                    "Final Broker Amount Received: %.2f\n",
                    finalAmount
                )
            )
            append(String.format(Locale.getDefault(), "Initial Amount: %.2f\n", initialAmount))
            append(String.format(Locale.getDefault(), "Exchange Rate: %.5f\n", exchangeRate))
            append(String.format(Locale.getDefault(), "Bank commission: %.2f\n", totalCommissions))
            append(
                String.format(
                    Locale.getDefault(),
                    "Final Amount: %.2f\n",
                    finalAmount
                )
            )
        }

        // Save to database
        val fundDepositResult = FundDepositResult(
            initialAmount = initialAmount,
            exchangeRate = exchangeRate,
            commissions = commissions.joinToString(","),
            finalAmount = finalAmount,
            transactionDate = transactionDate
        )
        viewModel.add(fundDepositResult)
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
                text = "CALCULATE COMMISSIONS",
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