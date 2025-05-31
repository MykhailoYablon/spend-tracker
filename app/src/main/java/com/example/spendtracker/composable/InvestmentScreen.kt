package com.example.spendtracker.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.spendtracker.model.Investment
import com.example.spendtracker.repository.Repository
import kotlinx.coroutines.launch

@Composable
fun InvestmentScreen(repository: Repository) {
    var showDialog by remember { mutableStateOf(false) }
    val investments by repository.getAllInvestments().collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()

    var investmentToEdit by remember { mutableStateOf<Investment?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Summary Card
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                border = BorderStroke(1.dp, Color.Black),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)

            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Total Investments",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        "$${investments.sumOf { it.amount }}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.primaryContainer
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
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primaryContainer
                        )
                        Text(
                            "No investments yet",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            } else {
                LazyColumn {
                    items(investments) { investment ->
                        InvestmentItem(
                            investment = investment,
                            onEdit = {
                                investmentToEdit = it // This opens the edit dialog
                            },
                            onDelete = {
                                coroutineScope.launch {
                                    repository.deleteInvestment(it)
                                }
                            })
                    }
                }
            }
        }

        // Edit Investment Dialog
        investmentToEdit?.let { investment ->
            InvestmentDialog(
                investment = investment, // Pass the investment to edit
                onDismiss = { investmentToEdit = null },
                onSave = { name, category, amount ->
                    val updatedInvestment = investment.copy(
                        name = name,
                        category = category,
                        amount = amount
                    )
                    coroutineScope.launch {
                        repository.updateInvestment(updatedInvestment)
                    }
                    investmentToEdit = null
                }
            )
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(36.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Investment", tint = Color.White)
        }
    }

    if (showDialog) {
        AddInvestmentDialog(
            onDismiss = { showDialog = false },
            onAdd = { name, category, amount ->
                coroutineScope.launch {
                    repository.insertInvestment(
                        Investment(name = name, category = category, amount = amount)
                    )
                }
                showDialog = false
            }
        )
    }
}