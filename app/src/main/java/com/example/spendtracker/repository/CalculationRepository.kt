package com.example.spendtracker.repository

import com.example.spendtracker.dao.CalculationDao
import com.example.spendtracker.model.CalculationResult
import kotlinx.coroutines.flow.Flow

class BondRepository(private val calculationDao: CalculationDao) {

    fun getAll(): Flow<List<CalculationResult>> = calculationDao.getAll()

    suspend fun insert(calculationResult: CalculationResult) = calculationDao.insert(calculationResult)

    suspend fun delete(calculationResult: CalculationResult) = calculationDao.delete(calculationResult)

    suspend fun deleteAll() = calculationDao.deleteAll()
}