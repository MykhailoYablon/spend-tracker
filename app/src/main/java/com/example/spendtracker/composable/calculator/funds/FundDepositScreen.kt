package com.example.spendtracker.composable.calculator.funds

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.spendtracker.model.FundDepositViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundDepositScreen(fundDepositViewModel: FundDepositViewModel) {

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Deposits", "History")
    var showInfoDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {

        Spacer(modifier = Modifier.height(18.dp))
        // Header
        Text(
            text = "FUNDS DEPOSITS",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(18.dp))
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Bond Return Calculator") },
                    actions = {
                        IconButton(onClick = { showInfoDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Information",
                                tint = Color(0xFF3AAB3E)
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color(0xFF3AAB3E),
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
                    0 -> FundDepositContent(viewModel = fundDepositViewModel)
                    1 -> FundDepositHistoryScreen(viewModel = fundDepositViewModel)
                }
            }
        }
    }

    if (showInfoDialog) {
        FundDepositInfoDialog(onDismiss = { showInfoDialog = false })
    }
}