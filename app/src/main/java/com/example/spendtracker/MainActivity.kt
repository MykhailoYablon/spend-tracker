package com.example.spendtracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.spendtracker.model.Transaction
import com.example.spendtracker.model.TransactionViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: TransactionViewModel
    private lateinit var adapter: TransactionAdapter
    private lateinit var transactions: List<Transaction> // keep a reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinner = findViewById<Spinner>(R.id.chartTypeSpinner)
        val barChart = findViewById<BarChart>(R.id.barChart)
        adapter = TransactionAdapter(emptyList())

        viewModel = ViewModelProvider(this)[TransactionViewModel::class.java]

        viewModel.allTransactions.observe(this) { txnList ->
            transactions = txnList
            adapter.updateList(txnList)
            updateBarChartGrouped(txnList, "MMM yyyy") // Default view: Monthly
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selected = parent.getItemAtPosition(position) as String
                when (selected) {
                    "Daily" -> updateBarChartGrouped(transactions, "dd MMM")
                    "Monthly" -> updateBarChartGrouped(transactions, "MMM yyyy")
                    "Yearly" -> updateBarChartGrouped(transactions, "yyyy")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        val addBtn = findViewById<FloatingActionButton>(R.id.addTransactionBtn)
        addBtn.setOnClickListener {
            showAddTransactionDialog()
        }
    }

    private fun showAddTransactionDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_transaction, null)
        val builder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Add Transaction")
            .setPositiveButton("Save") { _, _ ->
                val amount =
                    dialogView.findViewById<EditText>(R.id.editAmount).text.toString().toDouble()
                val category = dialogView.findViewById<EditText>(R.id.editCategory).text.toString()
                val note = dialogView.findViewById<EditText>(R.id.editNote).text.toString()

                val transaction = Transaction(
                    amount = amount,
                    category = category,
                    note = note,
                    date = System.currentTimeMillis()
                )
                viewModel.insert(transaction)
            }
            .setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun updateChart(transactions: List<Transaction>, pieChart: PieChart) {
        val categoryTotals = transactions
            .groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount } }

        val entries = categoryTotals.map {
            PieEntry(it.value.toFloat(), it.key)
        }

        val dataSet = PieDataSet(entries, "Expenses by Category").apply {
            setColors(ColorTemplate.MATERIAL_COLORS, 255)
            valueTextSize = 14f
        }

        val data = PieData(dataSet)
        pieChart.data = data
        pieChart.invalidate()
    }

    private fun updateBarChartGrouped(transactions: List<Transaction>, datePattern: String) {
        val sdf = SimpleDateFormat(datePattern, Locale.getDefault())
        val grouped = transactions.groupBy { sdf.format(Date(it.date)) }
            .mapValues { it.value.sumOf { txn -> txn.amount } }

        val entries = grouped.entries.mapIndexed { index, entry ->
            BarEntry(index.toFloat(), entry.value.toFloat())
        }

        val labels = grouped.keys.toList()

        val dataSet = BarDataSet(entries, "Spending")
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS, 255)
        dataSet.valueTextSize = 12f

        val barChart = findViewById<BarChart>(R.id.barChart)
        barChart.data = BarData(dataSet)
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        barChart.invalidate()

    }

    private fun updateBarChart(transactions: List<Transaction>, barChart: BarChart) {
        val sdf = SimpleDateFormat("MMM yyyy", Locale.getDefault())
        val monthlyTotals = transactions.groupBy {
            sdf.format(Date(it.date))
        }.mapValues { entry ->
            entry.value.sumOf { it.amount }
        }

        val entries = monthlyTotals.entries.mapIndexed { index, entry ->
            BarEntry(index.toFloat(), entry.value.toFloat())
        }

        val labels = monthlyTotals.keys.toList()

        val dataSet = BarDataSet(entries, "Monthly Spend").apply {
            setColors(ColorTemplate.MATERIAL_COLORS, 255)
            valueTextSize = 12f
        }

        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        barChart.xAxis.granularity = 1f
        barChart.data = BarData(dataSet)
        barChart.invalidate()
    }

}
