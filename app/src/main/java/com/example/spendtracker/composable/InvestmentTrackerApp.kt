package com.example.spendtracker.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.spendtracker.repository.Repository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvestmentTrackerApp(repository: Repository) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Investments", "Spendings", "Test")

    Column {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    "Financial Tracker",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = Color.White
            )
        )

        InvestmentSpendingCarousel(
            selectedTab = selectedTab,
            onTabChanged = { selectedTab = it },
            repository = repository
        )
    }
}
