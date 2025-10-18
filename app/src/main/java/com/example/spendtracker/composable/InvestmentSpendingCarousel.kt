package com.example.spendtracker.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.spendtracker.R
import com.example.spendtracker.composable.investment.InvestmentScreen
import com.example.spendtracker.composable.spending.SpendingScreen
import com.example.spendtracker.model.InvestmentViewModel
import com.example.spendtracker.model.SpendingViewModel
import com.example.spendtracker.util.AppConstants

@Composable
fun InvestmentSpendingCarousel(
    investmentViewModel: InvestmentViewModel,
    spendingViewModel: SpendingViewModel,
    onNavigateToInvestmentGraphs: () -> Unit,
    onNavigateToSpendingGraphs: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(
        initialPage = selectedTab,
        pageCount = { 2 }
    )

    // Sync pager state with selectedTab
    LaunchedEffect(selectedTab) {
        pagerState.animateScrollToPage(selectedTab)
    }

//    LaunchedEffect(pagerState.currentPage) {
//        onTabChanged(pagerState.currentPage)
//    }

    Column {
        // Carousel Header with Images
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(AppConstants.CAROUSEL_HEIGHT.dp),
        ) { page ->
            val title = when (page) {
                0 -> "Investments"
                else -> "Spendings"
            }
            val imageRes = when (page) {
                0 -> R.drawable.gold_stack
                else -> R.drawable.balance
            }
            CarouselCard(
                title = title,
                imageRes = imageRes,
                isSelected = page == pagerState.currentPage
            )
        }

        // Page Indicator
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppConstants.DEFAULT_PADDING.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(3) { index ->
                Box(
                    modifier = Modifier
                        .size(AppConstants.PAGE_INDICATOR_SIZE.dp)
                        .clip(CircleShape)
                        .background(
                            if (index == pagerState.currentPage)
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.outline
                        )
                        .padding(horizontal = AppConstants.SMALL_PADDING.dp)
                )
            }
        }

        // Content based on selected page
        when (pagerState.currentPage) {
            0 -> InvestmentScreen(
                investmentViewModel,
                onNavigateToGraphs = onNavigateToInvestmentGraphs
            )

            1 -> SpendingScreen(
                spendingViewModel,
                onNavigateToGraphs = onNavigateToSpendingGraphs
            )
        }
    }
}

@Composable
fun CarouselCard(
    title: String,
    imageRes: Int,
    isSelected: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = AppConstants.DEFAULT_PADDING.dp,
                vertical = AppConstants.SMALL_PADDING.dp
            ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) AppConstants.CARD_SELECTED_ELEVATION.dp else AppConstants.CARD_ELEVATION.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = "Icon for $title",
                    modifier = Modifier
                        .size(AppConstants.CAROUSEL_IMAGE_SIZE.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(AppConstants.DEFAULT_PADDING.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = if (isSelected)
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MaterialTheme.colorScheme.onSurface,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}
