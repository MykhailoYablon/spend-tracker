package com.example.spendtracker.model

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Double,
    val category: String,
    val note: String?,
    val date: Long // Timestamp in millis
)