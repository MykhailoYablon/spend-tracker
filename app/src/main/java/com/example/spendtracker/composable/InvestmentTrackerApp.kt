package com.example.spendtracker.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.spendtracker.composable.calculator.bond.CalculationScreen
import com.example.spendtracker.composable.calculator.funds.FundDepositScreen
import com.example.spendtracker.composable.graph.GraphsScreen
import com.example.spendtracker.ds.entity.Investment
import com.example.spendtracker.ds.entity.Spending
import com.example.spendtracker.model.CalculationViewModel
import com.example.spendtracker.model.FundDepositViewModel
import com.example.spendtracker.model.InvestmentViewModel
import com.example.spendtracker.model.SpendingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvestmentTrackerApp(
    investmentViewModel: InvestmentViewModel,
    spendingViewModel: SpendingViewModel,
    calculationViewModel: CalculationViewModel,
    fundDepositViewModel: FundDepositViewModel
) {
    var selectedBottomTab by remember { mutableIntStateOf(0) }
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
        Scaffold(
            containerColor = Color.Transparent,
            modifier = Modifier.padding(top = 30.dp),
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = selectedBottomTab == 0,
                        onClick = { selectedBottomTab = 0 },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Fund Deposits") },
                        label = { Text("Fund Deposits") }
                    )
                    NavigationBarItem(
                        selected = selectedBottomTab == 1,
                        onClick = { selectedBottomTab = 1 },
                        icon = { Icon(Icons.Default.Favorite, contentDescription = "Bonds") },
                        label = { Text("Bonds") }
                    )
                    NavigationBarItem(
                        selected = selectedBottomTab == 2,
                        onClick = { selectedBottomTab = 2 },
                        icon = { Icon(Icons.Filled.Info, contentDescription = "Tracker") },
                        label = { Text("Tracker") }
                    )
                    NavigationBarItem(
                        selected = selectedBottomTab == 3,
                        onClick = { selectedBottomTab = 3 },
                        icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                        label = { Text("Settings") }
                    )
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
//                    .background(Color.Black)
            ) {

                when (selectedBottomTab) {
                    0 -> FundDepositScreen(fundDepositViewModel)
                    1 -> CalculationScreen(calculationViewModel)
                    2 -> InvestmentSpendingCarousel(
                        investmentViewModel = investmentViewModel,
                        spendingViewModel = spendingViewModel,
                        onNavigateToInvestmentGraphs = { showInvestmentGraphs = true },
                        onNavigateToSpendingGraphs = { showSpendingGraphs = true }
                    )

                    3 -> SettingsScreen()
                }
            }
        }
    }
}

@Composable
fun FavoritesScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text(
            text = "Favorites Screen",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )
    }
}

@Composable
fun SettingsScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text(
            text = "Settings Screen",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )
    }
}

@Composable
fun getSharedGradient() = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFF2F1F5), // Blue
        Color(0xFF2F2754), // Purple
        Color.Black
    )
)
