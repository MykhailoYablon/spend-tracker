package com.example.spendtracker.ds.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.spendtracker.ds.entity.FundDepositResult
import kotlinx.coroutines.flow.Flow

@Dao
interface FundDepositDao {

    @Query("SELECT * FROM fund_deposits ORDER BY timestamp DESC")
    fun getAll(): Flow<List<FundDepositResult>>

    @Query("SELECT * FROM fund_deposits ORDER BY timestamp DESC")
    suspend fun getAllFundDeposits(): List<FundDepositResult>

    @Insert
    suspend fun insert(fundDeposit: FundDepositResult)

    @Delete
    suspend fun delete(fundDeposit: FundDepositResult)

    @Query("DELETE FROM fund_deposits")
    suspend fun deleteAll()
}