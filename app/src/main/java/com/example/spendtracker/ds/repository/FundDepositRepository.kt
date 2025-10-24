package com.example.spendtracker.ds.repository

import com.example.spendtracker.ds.dao.FundDepositDao
import com.example.spendtracker.ds.entity.FundDepositResult
import kotlinx.coroutines.flow.Flow

class FundDepositRepository(private val fundDepositDao: FundDepositDao) {

    fun getAll(): Flow<List<FundDepositResult>> = fundDepositDao.getAll()

    suspend fun getAllFundDeposits(): List<FundDepositResult> = fundDepositDao.getAllFundDeposits()

    suspend fun insert(fundDepositResult: FundDepositResult) = fundDepositDao.insert(fundDepositResult)

    suspend fun delete(fundDepositResult: FundDepositResult) = fundDepositDao.delete(fundDepositResult)

    suspend fun deleteAll() = fundDepositDao.deleteAll()
}