package com.example.spendtracker.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.spendtracker.model.Spending
import kotlinx.coroutines.flow.Flow

@Dao
interface SpendingDao {
    @Query("SELECT * FROM spendings ORDER BY date DESC")
    fun getAllSpendings(): Flow<List<Spending>>

    @Insert
    suspend fun insertSpending(spending: Spending)

    @Delete
    suspend fun deleteSpending(spending: Spending)
}