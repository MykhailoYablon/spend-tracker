package com.example.spendtracker.model

import com.example.spendtracker.repository.Repository

class InvestmentViewModel(private val repository: Repository) : androidx.lifecycle.ViewModel() {
    val investments = repository.getAllInvestments()

    suspend fun addInvestment(name: String, category: String, amount: Double) {
        repository.insertInvestment(Investment(name = name, category = category, amount = amount))
    }

    suspend fun deleteInvestment(investment: Investment) {
        repository.deleteInvestment(investment)
    }
}