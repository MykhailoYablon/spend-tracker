package com.example.spendtracker.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.example.spendtracker.model.Investment
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

@Composable
fun PieChartView(
    investments: List<Investment>,
    modifier: Modifier = Modifier
) {
    val groupData = remember(investments) {
        investments.groupBy { it.category }
            .mapValues { (_, investments) -> investments.sumOf { it.amount } }
    }

    AndroidView(
        factory = { context ->
            PieChart(context).apply {
                description.isEnabled = false
                isRotationEnabled = true
                setUsePercentValues(true)
                setEntryLabelColor(Color.Black.toArgb())
                setEntryLabelTextSize(12f)

                legend.apply {
                    verticalAlignment = Legend.LegendVerticalAlignment.TOP
                    horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                    orientation = Legend.LegendOrientation.VERTICAL
                    setDrawInside(false)
                    xEntrySpace = 7f
                    yEntrySpace = 0f
                    yOffset = 0f
                }
            }
        },
        modifier = modifier,
        update = { pieChart ->
            val entries = groupData.map { (group, amount) ->
                PieEntry(amount.toFloat(), group)
            }

            val dataSet = PieDataSet(entries, "Investment Categories").apply {
                colors = listOf(
                    Color(0xFFFF6B6B).toArgb(),
                    Color(0xFF4ECDC4).toArgb(),
                    Color(0xFF45B7D1).toArgb(),
                    Color(0xFFFFA07A).toArgb(),
                    Color(0xFF98D8C8).toArgb(),
                    Color(0xFFF7DC6F).toArgb(),
                    Color(0xFFBB8FCE).toArgb(),
                    Color(0xFF85C1E9).toArgb()
                )
                valueTextSize = 12f
                valueTextColor = Color.White.toArgb()
            }

            pieChart.data = PieData(dataSet)
            pieChart.invalidate()
        }
    )
}