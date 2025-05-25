package com.example.spendtracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "spendings")
data class Spending(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val category: String,
    val amount: Double,
    val date: Long = System.currentTimeMillis()
)