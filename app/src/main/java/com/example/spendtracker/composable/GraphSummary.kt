package com.example.spendtracker.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.spendtracker.model.Graph

@Composable
fun <T : Graph> GraphSummary(
    items: List<T>,
    modifier: Modifier = Modifier
) {
    val totalAmount = items.sumOf { it.amount }
    val groupCounts = items.groupBy { it.category }.mapValues { it.value.size }
    val average = if (items.isNotEmpty()) totalAmount / items.size else 0.0

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Summary Statistics",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Total Amount", style = MaterialTheme.typography.bodySmall)
                    Text(
                        "$${String.format("%.2f", totalAmount)}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column {
                    Text("Total ", style = MaterialTheme.typography.bodySmall)
                    Text(
                        "${items.size}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column {
                    Text("Average", style = MaterialTheme.typography.bodySmall)
                    Text(
                        "$${String.format("%.2f", average)}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (groupCounts.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "By Group:",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                groupCounts.forEach { (group, count) ->
                    Text(
                        text = "$group: $count investments",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}