package com.example.spendtracker.composable.calculator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.TabRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.spendtracker.model.CalculationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculationScreen(
    calculationViewModel: CalculationViewModel
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Calculation", "History")

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTab) {
            0 -> CalculationContent(calculationViewModel = calculationViewModel)
            1 -> HistoryScreen(viewModel = calculationViewModel)
        }
    }
}
