package com.example.spendtracker.ds.repository

import com.example.spendtracker.ds.dao.CalculationDao
import com.example.spendtracker.ds.entity.CalculationResult
import kotlinx.coroutines.flow.Flow

class CalculationRepository(private val calculationDao: CalculationDao) {

    fun getAll(): Flow<List<CalculationResult>> = calculationDao.getAll()

    suspend fun getAllCalculations(): List<CalculationResult> = calculationDao.getAllCalculations()

    suspend fun insert(calculationResult: CalculationResult) = calculationDao.insert(calculationResult)

    suspend fun delete(calculationResult: CalculationResult) = calculationDao.delete(calculationResult)

    suspend fun deleteAll() = calculationDao.deleteAll()
}