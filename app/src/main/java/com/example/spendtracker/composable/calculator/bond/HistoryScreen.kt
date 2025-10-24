package com.example.spendtracker.composable.calculator.bond

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.spendtracker.composable.calculator.common.DeleteAllDialog
import com.example.spendtracker.composable.calculator.common.HistoryCard
import com.example.spendtracker.ds.entity.CalculationResult
import com.example.spendtracker.model.CalculationViewModel
import java.util.Locale

@Composable
fun HistoryScreen(
    viewModel: CalculationViewModel = viewModel()
) {
    val context = LocalContext.current
    val calculations by viewModel.allCalculations.collectAsState(initial = emptyList())
    var showDeleteAllDialog by remember { mutableStateOf(false) }

    Scaffold { paddingValues ->
        if (calculations.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No calculations yet",
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
                            .height(50.dp),
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
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = calculations,
                        key = { it.id }
                    ) { calculation ->
                        CalculationCard(
                            calculation = calculation,
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
fun CalculationCard(
    calculation: CalculationResult,
    onDelete: () -> Unit
) {
    HistoryCard(
        timestamp = calculation.timestamp,
        onDelete = onDelete
    ) {
        Text(
            text = buildAnnotatedString {
                append(
                    String.format(
                        Locale.getDefault(),
                        "Theoretical Future Value: %.2f\n",
                        calculation.theoreticalFutureValue
                    )
                )
                append(
                    String.format(
                        Locale.getDefault(),
                        "Bank return: %.2f\n",
                        calculation.bankReturn
                    )
                )
                append(
                    String.format(
                        Locale.getDefault(),
                        "Real return: %.2f\n",
                        calculation.realReturn
                    )
                )
                append(
                    String.format(
                        Locale.getDefault(),
                        "Bank commission: %.2f\n",
                        calculation.bankCommission
                    )
                )
                append(
                    String.format(
                        Locale.getDefault(),
                        "Real %% (with commission): %.2f%%\n",
                        calculation.realPercentage
                    )
                )
                append(
                    String.format(
                        Locale.getDefault(),
                        "Return Date: %s",
                        calculation.returnDate
                    )
                )
            },
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = FontFamily.Monospace
        )
    }
}