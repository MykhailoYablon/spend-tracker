package com.example.spendtracker.composable.graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.example.spendtracker.enums.SortOption
import com.example.spendtracker.model.Graph
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

@Composable
fun <T : Graph> BarChartView(
    items: List<T>,
    sortOption: SortOption,
    modifier: Modifier = Modifier
) {
    val chartData = remember(items, sortOption) {
        when (sortOption) {
            SortOption.CATEGORY -> {
                items.groupBy { it.category }
                    .mapValues { (_, investments) -> investments.sumOf { it.amount } }
                    .toList()
            }

            else -> {
                items.groupBy { it.date.toString() }
                    .mapValues { (_, investments) -> investments.sumOf { it.amount } }
                    .toList()
                    .take(10) // Limit to recent 10 entries for readability
            }
        }
    }

    AndroidView(
        factory = { context ->
            BarChart(context).apply {
                description.isEnabled = false
                setMaxVisibleValueCount(10)
                setPinchZoom(false)
                setDrawBarShadow(false)
                setDrawGridBackground(false)

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    granularity = 1f
                    labelCount = chartData.size
                    labelRotationAngle = -45f
                }

                axisLeft.apply {
                    setDrawGridLines(false)
                    axisMinimum = 0f
                }

                axisRight.isEnabled = false

                legend.isEnabled = false
            }
        },
        modifier = modifier,
        update = { barChart ->
            val entries = chartData.mapIndexed { index, (_, amount) ->
                BarEntry(index.toFloat(), amount.toFloat())
            }

            val dataSet = BarDataSet(entries, "Investments").apply {
                Color(0xFFFF6B6B).toArgb()
                valueTextSize = 12f
                valueTextColor = Color.Black.toArgb()
            }

            barChart.data = BarData(dataSet)
            barChart.xAxis.valueFormatter = IndexAxisValueFormatter(chartData.map { it.first })
            barChart.invalidate()
        }
    )
}