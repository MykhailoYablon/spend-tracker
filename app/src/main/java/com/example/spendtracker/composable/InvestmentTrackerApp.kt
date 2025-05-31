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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.spendtracker.repository.Repository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvestmentTrackerApp(repository: Repository) {
    var selectedTab by remember { mutableIntStateOf(0) }

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
                containerColor = Color(0xFF1E3A8A),
                titleContentColor = Color.White
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
                repository = repository
            )
        }
    }
}

@Composable
fun getSharedGradient() = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF1E3A8A), // Dark blue
        Color(0xFF3B82F6), // Lighter blue
        Color(0xFF8B5CF6)  // Purple
    )
)
