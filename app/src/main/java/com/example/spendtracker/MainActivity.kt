package com.example.spendtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.material3.MaterialTheme
import com.example.spendtracker.composable.InvestmentTrackerApp
import androidx.activity.compose.setContent
import androidx.room.Room
import com.example.spendtracker.ds.AppDatabase
import com.example.spendtracker.repository.Repository

class MainActivity : ComponentActivity() {

    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "investment_tracker_db"
        ).build()
    }

    private val repository by lazy {
        Repository(database.investmentDao(), database.spendingDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                InvestmentTrackerApp(repository)
            }
        }
    }
}
