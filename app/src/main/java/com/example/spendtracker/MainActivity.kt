package com.example.spendtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.spendtracker.composable.InvestmentTrackerApp
import com.example.spendtracker.ds.AppDatabase
import com.example.spendtracker.ds.repository.CalculationRepository
import com.example.spendtracker.ds.repository.FundDepositRepository
import com.example.spendtracker.ds.repository.Repository
import com.example.spendtracker.model.CalculationViewModel
import com.example.spendtracker.model.FundDepositViewModel
import com.example.spendtracker.model.InvestmentViewModel
import com.example.spendtracker.model.SpendingViewModel

class MainActivity : ComponentActivity() {

    private val database by lazy {
        AppDatabase.getDatabase(this)
    }

    private val repository by lazy {
        Repository(database.investmentDao(), database.spendingDao())
    }

    private val calculationRepository by lazy {
        CalculationRepository(database.calculationDao())
    }

    private val fundDepositRepository by lazy {
        FundDepositRepository(database.fundDepositDao())
    }

    private val investmentViewModel by lazy {
        InvestmentViewModel(repository)
    }

    private val spendingViewModel by lazy {
        SpendingViewModel(repository)
    }

    private val calculationViewModel by lazy {
        CalculationViewModel(calculationRepository)
    }
    private val fundDepositViewModel by lazy {
        FundDepositViewModel(fundDepositRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var useDarkTheme by rememberSaveable { mutableStateOf(true) }

            MaterialTheme(
                colorScheme = if (useDarkTheme) darkColorScheme() else lightColorScheme()
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    InvestmentTrackerApp(
                        investmentViewModel = investmentViewModel,
                        spendingViewModel = spendingViewModel,
                        calculationViewModel = calculationViewModel,
                        fundDepositViewModel = fundDepositViewModel
                    )
                }
            }
        }
        val controller = WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView())
        controller.hide(WindowInsetsCompat.Type.systemBars())
    }
}
