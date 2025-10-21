package com.example.spendtracker.ds.repository

import com.example.spendtracker.ds.dao.CommissionDao
import com.example.spendtracker.ds.entity.Commission
import kotlinx.coroutines.flow.Flow

class CommissionRepository(private val commissionDao: CommissionDao) {

    fun getAll(): Flow<List<Commission>> = commissionDao.getAll()

    suspend fun insert(commission: Commission) = commissionDao.insert(commission)

    suspend fun delete(commission: Commission) = commissionDao.delete(commission)

    suspend fun deleteAll() = commissionDao.deleteAll()
}