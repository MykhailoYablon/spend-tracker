package com.example.spendtracker.composable.calculator.bond

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CalculationInfoDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Information",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text(
                text = "Bond Return Calculator",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "This calculator helps you analyze bond returns and compare them with bank offers.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "How to use:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "1. Enter the purchase price of the bond\n" +
                            "2. Enter the bank's return amount\n" +
                            "3. Enter the coupon rate (annual interest %)\n" +
                            "4. Add any bank commissions\n" +
                            "5. Select the return date\n" +
                            "6. Press 'Calculate' to see results",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Results include:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "• Theoretical future value\n" +
                            "• Real return and percentage\n" +
                            "• Bank commission comparison\n" +
                            "• Annualized return rate",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "All calculations are automatically saved to History.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontStyle = FontStyle.Italic
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Got it")
            }
        }
    )
}