package com.example.spendtracker.composable.calculator.funds

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.spendtracker.composable.calculator.bond.DeleteAllDialog
import com.example.spendtracker.ds.entity.FundDepositResult
import com.example.spendtracker.model.FundDepositViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun FundDepositHistoryScreen(viewModel: FundDepositViewModel) {
    val context = LocalContext.current
    val fundDeposits by viewModel.allFundDeposits.collectAsState(initial = emptyList())
    var showDeleteAllDialog by remember { mutableStateOf(false) }

    Scaffold { paddingValues ->
        if (fundDeposits.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No funds deposits yet",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues) // Apply padding once to the parent Column
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            viewModel.exportCalculations(context)
                        },
                        modifier = Modifier
                            .height(45.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF803BFF)
                        )
                    ) {
                        Text(
                            text = "Export to CSV",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = fundDeposits,
                        key = { it.id }
                    ) { calculation ->
                        FundDepositCard(
                            fundDepositResult = calculation,
                            onDelete = { viewModel.delete(calculation) }
                        )
                    }
                }
            }
        }
    }

    if (showDeleteAllDialog) {
        DeleteAllDialog(
            onConfirm = {
                viewModel.deleteAll()
                showDeleteAllDialog = false
            },
            onDismiss = { showDeleteAllDialog = false }
        )
    }
}

@Composable
fun FundDepositCard(
    fundDepositResult: FundDepositResult,
    onDelete: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dateFormat.format(Date(fundDepositResult.timestamp)),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = buildAnnotatedString {
                    append(
                        String.format(
                            Locale.getDefault(),
                            "Initial Amount: %.2f\n",
                            fundDepositResult.initialAmount
                        )
                    )
                    append(
                        String.format(
                            Locale.getDefault(),
                            "Exchange Rate: %.5f\n",
                            fundDepositResult.exchangeRate
                        )
                    )
                    append(
                        String.format(
                            Locale.getDefault(),
                            "Bank commissions: %s\n",
                            fundDepositResult.commissions
                        )
                    )
                    append(
                        String.format(
                            Locale.getDefault(),
                            "Final Amount: %.2f\n",
                            fundDepositResult.finalAmount
                        )
                    )
                    append(
                        String.format(
                            Locale.getDefault(),
                            "Transaction Date: %s",
                            fundDepositResult.transactionDate
                        )
                    )
                },
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}