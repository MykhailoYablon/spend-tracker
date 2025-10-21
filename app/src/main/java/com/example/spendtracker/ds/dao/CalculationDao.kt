package com.example.spendtracker.ds.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.spendtracker.ds.entity.CalculationResult
import kotlinx.coroutines.flow.Flow

@Dao
interface CalculationDao {

    @Query("SELECT * FROM calculations ORDER BY timestamp DESC")
    fun getAll(): Flow<List<CalculationResult>>

    @Insert
    suspend fun insert(calculationResult: CalculationResult)

    @Delete
    suspend fun delete(calculationResult: CalculationResult)

    @Query("DELETE FROM calculations")
    suspend fun deleteAll()
}