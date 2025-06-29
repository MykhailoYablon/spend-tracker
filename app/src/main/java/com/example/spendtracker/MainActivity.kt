package com.example.spendtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.spendtracker.composable.InvestmentTrackerApp
import com.example.spendtracker.ds.AppDatabase
import com.example.spendtracker.model.InvestmentViewModel
import com.example.spendtracker.model.SpendingViewModel
import com.example.spendtracker.repository.Repository
import com.example.spendtracker.theme.AppTheme
import com.example.spendtracker.theme.ThemeManager

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

    private val investmentViewModel by lazy {
        InvestmentViewModel(repository)
    }

    private val spendingViewModel by lazy {
        SpendingViewModel(repository)
    }

    private val themeManager by lazy {
        ThemeManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(themeManager = themeManager) {
                InvestmentTrackerApp(
                    investmentViewModel = investmentViewModel,
                    spendingViewModel = spendingViewModel
                )
            }
        }
        val controller = androidx.core.view.WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView())
        controller.hide(androidx.core.view.WindowInsetsCompat.Type.systemBars())
    }
}
