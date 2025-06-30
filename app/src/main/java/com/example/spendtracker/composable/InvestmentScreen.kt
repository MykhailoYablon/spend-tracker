package com.example.spendtracker.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.spendtracker.model.InvestmentViewModel
import com.example.spendtracker.util.AppConstants

@Composable
fun InvestmentScreen(
    viewModel: InvestmentViewModel,
    onNavigateToGraphs: () -> Unit
) {
    val investments by viewModel.investments.collectAsState(initial = emptyList())
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val totalAmount = investments.sumOf { it.amount }
    val totalCount = investments.size

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(AppConstants.DEFAULT_PADDING.dp)
        ) {
            // Summary Card
            TotalInvestmentsCard(
                totalAmount = totalAmount,
                totalCount = totalCount,
                onGraphClick = onNavigateToGraphs
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.padding(vertical = AppConstants.SMALL_PADDING.dp)
            )

            // Investments List
            if (investments.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Build,
                            contentDescription = "No investments icon",
                            modifier = Modifier.size(AppConstants.ICON_SIZE.dp),
                            tint = MaterialTheme.colorScheme.primaryContainer
                        )
                        Text(
                            "No investments yet",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = AppConstants.SMALL_PADDING.dp)
                        )
                    }
                }
            } else {
                LazyColumn {
                    items(
                        items = investments,
                        key = { it.id }
                    ) { investment ->
                        InvestmentItem(
                            investment = investment,
                            onEdit = { viewModel.editInvestment(it) },
                            onDelete = { viewModel.deleteInvestment(it) }
                        )
                    }
                }
            }
        }

        // Edit Investment Dialog
        uiState.investmentToEdit?.let { investment ->
            InvestmentDialog(
                investment = investment,
                onDismiss = { viewModel.hideDialog() },
                onSave = { name, category, amount ->
                    viewModel.updateInvestment(investment, name, category, amount)
                }
            )
        }

        // Add Investment Dialog
        if (uiState.showDialog) {
            AddInvestmentDialog(
                onDismiss = { viewModel.hideDialog() },
                onAdd = { name, category, amount ->
                    viewModel.addInvestment(name, category, amount)
                }
            )
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = { viewModel.showAddDialog() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(AppConstants.FAB_PADDING.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Add Investment",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        // Snackbar for error messages
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

    // Show error messages
    uiState.errorMessage?.let { errorMessage ->
        LaunchedEffect(errorMessage) {
            snackbarHostState.showSnackbar(errorMessage)
            viewModel.clearError()
        }
    }
}