package com.example.spendtracker.ds

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.spendtracker.dao.InvestmentDao
import com.example.spendtracker.dao.SpendingDao
import com.example.spendtracker.model.Investment
import com.example.spendtracker.model.Spending

@Database(
    entities = [Investment::class, Spending::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun investmentDao(): InvestmentDao
    abstract fun spendingDao(): SpendingDao
}
