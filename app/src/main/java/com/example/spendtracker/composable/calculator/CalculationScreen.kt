package com.example.spendtracker.composable.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendtracker.model.CalculationResult
import com.example.spendtracker.model.CalculationViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.math.pow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BondScreen(
    calculationViewModel: CalculationViewModel
) {
    var purchasePriceText by remember { mutableStateOf("1000") }
    var couponRateText by remember { mutableStateOf("17") }
    var bankReturnText by remember { mutableStateOf("1170") }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
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
        val date = selectedDate
        if (price == null || couponRate == null || date == null || bankReturn == null) {
            resultText = "Please enter valid price, yield, bank return, and date."
            return
        }
        val today = LocalDate.now()
        val days = ChronoUnit.DAYS.between(today, date).coerceAtLeast(0)
        val years = days / 365.25
        val theoreticalFutureValue = price * (1.0 + (couponRate / 100.0)).pow(years)
        val realReturn = bankReturn - price
        val bankCommission = theoreticalFutureValue - bankReturn
        val realPercentage = if (years > 0.0) {
            (((bankReturn / price).pow(1.0 / years) - 1.0) * 100.0)
        } else {
            // If same day, fall back to simple (non-annualized) percentage
            ((realReturn / price) * 100.0)
        }
        resultText = buildString {
            append(String.format(Locale.getDefault(), "Theoretical Future Value: %.2f\n", theoreticalFutureValue))
            append(String.format(Locale.getDefault(), "Bank return: %.2f\n", bankReturn))
            append(String.format(Locale.getDefault(), "Real return: %.2f\n", realReturn))
            append(String.format(Locale.getDefault(), "Bank commission: %.2f\n", bankCommission))
            // Escape the literal percent before "(with commission)" and at the end
            append(String.format(Locale.getDefault(), "Real %% (with commission): %.2f%%", realPercentage))
        }

        // Save to database
        val calculationResult = CalculationResult(
            theoreticalFutureValue = theoreticalFutureValue,
            bankReturn = bankReturn,
            realReturn = realReturn,
            bankCommission = bankCommission,
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
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Header
        Text(
            text = "BOND RETURN CALCULATOR",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )

        // Tab Row (Calculate/History)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "CALCULATE",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF4A90E2),
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
            Text(
                text = "HISTORY",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.6f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

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
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
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
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4A90E2)
            )
        ) {
            Text(
                text = "CALCULATE RETURN",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                color = Color.White
            )
        }

//        Spacer(modifier = Modifier.height(10.dp))

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
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun SplitInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.3f),
                shape = RoundedCornerShape(8.dp)
            )
            .background(Color(0xFF2A2A2A))
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left half - Label
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = label,
                    color = Color.White,
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            
            // Right half - Input value
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .background(Color.DarkGray)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                androidx.compose.foundation.text.BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        textAlign = TextAlign.End
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun textFieldColors() = TextFieldDefaults.colors(
    focusedLabelColor = Color.White,
    unfocusedLabelColor = Color.White,
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    cursorColor = Color.White,
    focusedIndicatorColor = Color(0xFF4A90E2),
    unfocusedIndicatorColor = Color.White.copy(alpha = 0.6f)
)
