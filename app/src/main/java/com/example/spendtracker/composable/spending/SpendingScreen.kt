package com.example.spendtracker.composable.spending

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
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
import com.example.spendtracker.model.SpendingViewModel
import com.example.spendtracker.util.AppConstants

@Composable
fun SpendingScreen(
    viewModel: SpendingViewModel,
    onNavigateToGraphs: () -> Unit
) {
    val spendings by viewModel.spendings.collectAsState(initial = emptyList())
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val totalAmount = spendings.sumOf { it.amount }
    val totalCount = spendings.size

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(AppConstants.DEFAULT_PADDING.dp)
        ) {
            // Summary Card
            TotalSpendingCard(
                totalAmount = totalAmount,
                totalCount = totalCount,
                onGraphClick = onNavigateToGraphs
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.padding(vertical = AppConstants.SMALL_PADDING.dp)
            )

            // Spendings List
            if (spendings.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = "No spendings icon",
                            modifier = Modifier.size(AppConstants.ICON_SIZE.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            "No spendings yet",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = AppConstants.SMALL_PADDING.dp)
                        )
                    }
                }
            } else {
                LazyColumn {
                    items(
                        items = spendings,
                        key = { it.id }
                    ) { spending ->
                        SpendingItem(
                            spending = spending,
                            onDelete = { viewModel.deleteSpending(spending) }
                        )
                    }
                }
            }
        }

        // Add Spending Dialog
        if (uiState.showDialog) {
            AddSpendingDialog(
                onDismiss = { viewModel.hideDialog() },
                onAdd = { name, category, amount ->
                    viewModel.addSpending(name, category, amount)
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
                contentDescription = "Add Spending",
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

