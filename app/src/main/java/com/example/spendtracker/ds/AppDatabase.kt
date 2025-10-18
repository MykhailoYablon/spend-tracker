package com.example.spendtracker.ds

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.spendtracker.dao.CalculationDao
import com.example.spendtracker.dao.InvestmentDao
import com.example.spendtracker.dao.SpendingDao
import com.example.spendtracker.model.CalculationResult
import com.example.spendtracker.model.Investment
import com.example.spendtracker.model.Spending

@Database(
    entities = [Investment::class, Spending::class, CalculationResult::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun investmentDao(): InvestmentDao
    abstract fun spendingDao(): SpendingDao

    abstract fun calculationDao(): CalculationDao
}
