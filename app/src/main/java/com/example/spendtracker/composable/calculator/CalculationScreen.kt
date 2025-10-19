package com.example.spendtracker.composable.calculator

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TabPosition
import androidx.compose.material.TabRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.spendtracker.model.CalculationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculationScreen(
    calculationViewModel: CalculationViewModel
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Calculation", "History")



    Column(modifier = Modifier.fillMaxSize()) {

        Spacer(modifier = Modifier.height(18.dp))
        // Header
        Text(
            text = "BOND RETURN CALCULATOR",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(18.dp))

        TabRow(
            selectedTabIndex = selectedTab,
            backgroundColor = Color(0xFF4A90E2),
//            divider = VerticalDivider(modifier = Modifier.fillMaxSize()),
            modifier = Modifier
                .padding(5.dp)
                .height(45.dp)
                .clip(
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) },
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(20.dp))
                        .padding(bottom = 3.dp)
                        .zIndex(2f),
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color.Black
                )
            }
        }

        when (selectedTab) {
            0 -> CalculationContent(calculationViewModel = calculationViewModel)
            1 -> HistoryScreen(viewModel = calculationViewModel)
        }
    }
}
