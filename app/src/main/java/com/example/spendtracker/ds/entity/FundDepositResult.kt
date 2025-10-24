package com.example.spendtracker.ds.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "fund_deposits")
data class FundDepositResult(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val initialAmount: Double,
    val exchangeRate: Double,
    val commissions: String,
    val finalAmount: Double,
    val transactionDate: LocalDate?,
    val timestamp: Long = System.currentTimeMillis()
)