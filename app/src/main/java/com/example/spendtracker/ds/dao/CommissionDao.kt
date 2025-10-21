package com.example.spendtracker.ds.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.spendtracker.ds.entity.Commission
import kotlinx.coroutines.flow.Flow

@Dao
interface CommissionDao {

    @Query("SELECT * FROM commissions ORDER BY timestamp DESC")
    fun getAll(): Flow<List<Commission>>

    @Insert
    suspend fun insert(сommission: Commission)

    @Delete
    suspend fun delete(сommission: Commission)

    @Query("DELETE FROM commissions")
    suspend fun deleteAll()
}