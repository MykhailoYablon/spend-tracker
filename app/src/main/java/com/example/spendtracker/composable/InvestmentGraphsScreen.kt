package com.example.spendtracker.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.spendtracker.enums.ChartType
import com.example.spendtracker.enums.SortOption
import com.example.spendtracker.model.Investment


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvestmentGraphsScreen(
    investments: List<Investment>,
    onBackClick: () -> Unit

) {
    var selectedSortOption by remember { mutableStateOf(SortOption.DATE_DESC) }
    var selectedChartType by remember { mutableStateOf(ChartType.PIE) }

    val sortedInvestments = remember(investments, selectedSortOption) {
        when (selectedSortOption) {
            SortOption.DATE_ASC -> investments.sortedBy { it.date }
            SortOption.DATE_DESC -> investments.sortedByDescending { it.date }
            SortOption.CATEGORY -> investments.sortedBy { it.category }
            SortOption.AMOUNT_ASC -> investments.sortedBy { it.amount }
            SortOption.AMOUNT_DESC -> investments.sortedByDescending { it.amount }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = getSharedGradient())
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Investment Analytics") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF1E3A8A),
                titleContentColor = Color.White
            )
        )


        // Controls
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    onClick = { selectedChartType = ChartType.PIE },
                    label = { Text("Pie Chart") },
                    selected = selectedChartType == ChartType.PIE,
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.KeyboardArrowDown,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )
            }
            item {
                FilterChip(
                    onClick = { selectedChartType = ChartType.BAR },
                    label = { Text("Bar Chart") },
                    selected = selectedChartType == ChartType.BAR,
                    leadingIcon = {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )
            }
        }

        // Sort Options
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(SortOption.entries.size) { index ->
                val option = SortOption.entries[index]
                FilterChip(
                    onClick = { selectedSortOption = option },
                    label = { Text(option.displayName) },
                    selected = selectedSortOption == option
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Chart Display
        when (selectedChartType) {
            ChartType.PIE -> {
                PieChartView(
                    investments = sortedInvestments,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(16.dp)
                )
            }

            ChartType.BAR -> {
                BarChartView(
                    investments = sortedInvestments,
                    sortOption = selectedSortOption,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(16.dp)
                )
            }
        }

        // Summary Statistics
        InvestmentSummary(
            investments = sortedInvestments,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}