package com.example.spendtracker.repository

import com.example.spendtracker.dao.InvestmentDao
import com.example.spendtracker.dao.SpendingDao
import com.example.spendtracker.model.Investment
import com.example.spendtracker.model.Spending
import kotlinx.coroutines.flow.Flow

class Repository(
    private val investmentDao: InvestmentDao,
    private val spendingDao: SpendingDao
) {
    fun getAllInvestments(): Flow<List<Investment>> = investmentDao.getAllInvestments()
    fun getAllSpendings(): Flow<List<Spending>> = spendingDao.getAllSpendings()

    suspend fun insertInvestment(investment: Investment) = investmentDao.insertInvestment(investment)
    suspend fun insertSpending(spending: Spending) = spendingDao.insertSpending(spending)

    suspend fun deleteInvestment(investment: Investment) = investmentDao.deleteInvestment(investment)
    suspend fun deleteSpending(spending: Spending) = spendingDao.deleteSpending(spending)
}