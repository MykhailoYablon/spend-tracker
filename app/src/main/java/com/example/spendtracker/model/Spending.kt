package com.example.spendtracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "spendings")
data class Spending(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    override val category: String,
    override val amount: Double,
    override val date: Long = System.currentTimeMillis()
) : Graph