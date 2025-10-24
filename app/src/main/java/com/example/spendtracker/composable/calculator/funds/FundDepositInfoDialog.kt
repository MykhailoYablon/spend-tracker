package com.example.spendtracker.composable.calculator.funds

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
fun FundDepositInfoDialog(onDismiss: () -> Unit) {
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
                text = "Funds Deposits",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "This calculator helps you track international fund deposits and calculate the final amount after exchange rate conversion and commissions.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "How to use:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "1. Enter the initial amount in your currency\n" +
                            "2. Enter the exchange rate\n" +
                            "3. Add any bank commissions (you can add multiple)\n" +
                            "4. Enter the amount received in broker account\n" +
                            "5. Select the transaction date\n" +
                            "6. Press 'Add Fund Deposit' to save",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Features:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "• Track multiple bank commissions\n" +
                            "• Calculate net amount after conversions\n" +
                            "• Keep history of all deposits\n" +
                            "• Export data to CSV format",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "All deposits are automatically saved to History for future reference.",
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