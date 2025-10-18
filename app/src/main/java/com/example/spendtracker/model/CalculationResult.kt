package com.example.spendtracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bonds")
data class Calculation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val theoreticalFutureValue: Double,
    val bankReturn: Double,
    val realReturn: Double,
    val bankCommission: Double,
    val realPercentage: Double,
    val timestamp: Long = System.currentTimeMillis()
)