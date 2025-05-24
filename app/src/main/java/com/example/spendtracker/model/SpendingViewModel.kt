package com.example.spendtracker.model

import com.example.spendtracker.repository.Repository

class SpendingViewModel(private val repository: Repository) : androidx.lifecycle.ViewModel() {
    val spendings = repository.getAllSpendings()

    suspend fun addSpending(name: String, category: String, amount: Double) {
        repository.insertSpending(Spending(name = name, category = category, amount = amount))
    }

    suspend fun deleteSpending(spending: Spending) {
        repository.deleteSpending(spending)
    }
}