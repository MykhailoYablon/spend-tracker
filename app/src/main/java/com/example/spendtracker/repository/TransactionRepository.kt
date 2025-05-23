package com.example.spendtracker.repository

import com.example.spendtracker.dao.TransactionDao
import com.example.spendtracker.model.Transaction

class TransactionRepository(private val dao: TransactionDao) {
    val allTransactions = dao.getAllTransactions()

    suspend fun insert(transaction: Transaction) {
        dao.insert(transaction)
    }

    suspend fun delete(transaction: Transaction) {
        dao.delete(transaction)
    }
}
