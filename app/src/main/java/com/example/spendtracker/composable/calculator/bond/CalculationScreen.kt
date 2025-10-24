package com.example.spendtracker.composable.calculator.bond

import android.content.Intent
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current
    val exportUri by calculationViewModel.exportEvent.observeAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Calculation", "History")
    var showInfoDialog by remember { mutableStateOf(false) }


    LaunchedEffect(exportUri) {
        exportUri?.let { uri ->
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/csv"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(shareIntent, "Export Calculations"))
            calculationViewModel.onExportHandled()
        }
    }

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
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Bond Return Calculator") },
                    actions = {
                        IconButton(onClick = { showInfoDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Information",
                                tint = MaterialTheme.colorScheme.primary
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
                    containerColor = Color(0xFF4A90E2),
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
    }

    if (showInfoDialog) {
        CalculationInfoDialog(onDismiss = { showInfoDialog = false })
    }
}
