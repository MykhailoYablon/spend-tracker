package com.example.spendtracker.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.spendtracker.model.Investment
import kotlinx.coroutines.flow.Flow

@Dao
interface InvestmentDao {
    @Query("SELECT * FROM investments ORDER BY date DESC")
    fun getAllInvestments(): Flow<List<Investment>>

    @Insert
    suspend fun insertInvestment(investment: Investment)

    @Delete
    suspend fun deleteInvestment(investment: Investment)
}