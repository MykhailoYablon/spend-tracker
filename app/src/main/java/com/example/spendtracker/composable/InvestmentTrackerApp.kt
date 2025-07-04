package com.example.spendtracker.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.spendtracker.composable.graph.GraphsScreen
import com.example.spendtracker.model.Investment
import com.example.spendtracker.model.InvestmentViewModel
import com.example.spendtracker.model.Spending
import com.example.spendtracker.model.SpendingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvestmentTrackerApp(
    investmentViewModel: InvestmentViewModel,
    spendingViewModel: SpendingViewModel
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showInvestmentGraphs by remember { mutableStateOf(false) }
    var showSpendingGraphs by remember { mutableStateOf(false) }
    var investments by remember { mutableStateOf(emptyList<Investment>()) }
    var spending by remember { mutableStateOf(emptyList<Spending>()) }

    // Collect investments for the graphs
    LaunchedEffect(Unit) {
        investmentViewModel.investments.collect { investmentList ->
            investments = investmentList

            spendingViewModel.spendings.collect { spendingList ->
                spending = spendingList
            }
        }
    }

    if (showInvestmentGraphs) {
        // Show the graphs screen
        GraphsScreen(
            items = investments,
            onBackClick = { showInvestmentGraphs = false }
        )
    } else if (showSpendingGraphs) {
        GraphsScreen(
            items = spending,
            onBackClick = { showSpendingGraphs = false }

        )
    } else {
        Column {
            // Top App Bar
            TopAppBar(
                title = {
                    Text(
                        "Financial Tracker",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                    titleContentColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary
                )
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(brush = getSharedGradient())
            ) {
                InvestmentSpendingCarousel(
                    selectedTab = selectedTab,
                    onTabChanged = { selectedTab = it },
                    investmentViewModel = investmentViewModel,
                    spendingViewModel = spendingViewModel,
                    onNavigateToInvestmentGraphs = { showInvestmentGraphs = true },
                    onNavigateToSpendingGraphs = { showSpendingGraphs = true }
                )
            }
        }
    }
}

@Composable
fun getSharedGradient() = Brush.verticalGradient(
    colors = listOf(
        androidx.compose.material3.MaterialTheme.colorScheme.primary,
        androidx.compose.material3.MaterialTheme.colorScheme.secondary,
        androidx.compose.material3.MaterialTheme.colorScheme.tertiary
    )
)
