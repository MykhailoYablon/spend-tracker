package com.example.spendtracker.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.spendtracker.model.Calculation
import kotlinx.coroutines.flow.Flow

interface CalculationDao {

    @Query("SELECT * FROM bonds ORDER BY timestamp DESC")
    fun getAllBonds(): Flow<List<Calculation>>

    @Insert
    suspend fun insertBond(calculation: Calculation)

    @Delete
    suspend fun deleteBond(calculation: Calculation)
}