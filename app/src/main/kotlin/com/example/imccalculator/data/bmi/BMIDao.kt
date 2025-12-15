package com.example.imccalculator.data.bmi

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BMIDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: BMIEntity)


    @Delete
    suspend fun delete (entity: BMIEntity)

    @Query("DELETE FROM bmis")
    suspend fun deleteAll()

    @Query("SELECT * FROM bmis ORDER BY timestamp DESC")
    fun getAll(): Flow<List<BMIEntity>>

    @Query("SELECT * FROM bmis WHERE id = :id")
    suspend fun getById(id: Long): BMIEntity
}