package com.example.spendtracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "calculations")
data class CalculationResult(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val theoreticalFutureValue: Double,
    val bankReturn: Double,
    val realReturn: Double,
    val bankCommission: Double,
    val realPercentage: Double,
    val returnDate: LocalDate?,
    val timestamp: Long = System.currentTimeMillis()
)