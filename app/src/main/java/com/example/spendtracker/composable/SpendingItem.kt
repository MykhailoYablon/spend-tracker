package com.example.spendtracker.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.spendtracker.model.Spending
import com.example.spendtracker.util.AppConstants
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SpendingItem(
    spending: Spending,
    onDelete: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = AppConstants.SMALL_PADDING.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = AppConstants.CARD_ELEVATION.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppConstants.DEFAULT_PADDING.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    spending.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    spending.category,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    SimpleDateFormat(
                        "MMM dd, yyyy",
                        Locale.getDefault()
                    ).format(Date(spending.date)),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppConstants.SMALL_PADDING.dp)
            ) {
                Text(
                    "-$${spending.amount}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )

                onDelete?.let {
                    IconButton(
                        onClick = it,
                        modifier = Modifier.size(AppConstants.ICON_SIZE.dp / 2)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete spending",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(AppConstants.ICON_SIZE.dp / 2)
                        )
                    }
                }
            }
        }
    }
}