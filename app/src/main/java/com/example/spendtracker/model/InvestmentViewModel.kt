package com.example.spendtracker.model

import androidx.lifecycle.viewModelScope
import com.example.spendtracker.repository.Repository
import kotlinx.coroutines.launch

class InvestmentViewModel(private val repository: Repository) : androidx.lifecycle.ViewModel() {
    val investments = repository.getAllInvestments()

    suspend fun addInvestment(name: String, category: String, amount: Double) {
        repository.insertInvestment(Investment(name = name, category = category, amount = amount))
    }

    suspend fun getInvestmentDetails(id: Long) {
        repository.getInvestmentDetails(id)
    }

    suspend fun deleteInvestment(investment: Investment) {
        repository.deleteInvestment(investment)
    }

    fun updateInvestment(investment: Investment) {
        viewModelScope.launch {
            repository.updateInvestment(investment)
        }
    }
}