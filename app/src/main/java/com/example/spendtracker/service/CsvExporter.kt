package com.example.spendtracker.service

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.spendtracker.ds.entity.CalculationResult
import java.io.File
import java.io.FileWriter
import java.time.format.DateTimeFormatter

class CsvExporter(private val context: Context) {

    fun exportCalculationsToCSV(calculations: List<CalculationResult>): Uri? {
        if (calculations.isEmpty()) return null

        return try {
            val csvFile = createCSVFile(calculations)
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                csvFile
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun createCSVFile(calculations: List<CalculationResult>): File {
        val fileName = "calculations_export_${System.currentTimeMillis()}.csv"
        val file = File(context.cacheDir, fileName)

        FileWriter(file).use { writer ->
            // Write header
            writer.append("ID,Theoretical Future Value,Bank Return,Real Return,Bank Commission,Real Percentage,Return Date,Timestamp\n")

            // Write data rows
            val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
            calculations.forEach { calc ->
                writer.append("${calc.id},")
                writer.append("${calc.theoreticalFutureValue},")
                writer.append("${calc.bankReturn},")
                writer.append("${calc.realReturn},")
                writer.append("${calc.bankCommission},")
                writer.append("${calc.realPercentage},")
                writer.append("${calc.returnDate?.format(dateFormatter) ?: ""},")
                writer.append("${calc.timestamp}\n")
            }
        }

        return file
    }

    fun shareCSV(uri: Uri) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Export Calculations"))
    }
}