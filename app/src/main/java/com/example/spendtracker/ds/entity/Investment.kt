package com.example.spendtracker.ds.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.spendtracker.model.Graph

@Entity(tableName = "investments")
data class Investment (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    override val category: String,
    override val amount: Double,
    override val date: Long = System.currentTimeMillis()
) : Graph