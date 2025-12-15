package com.example.imccalculator.data.bmi

import com.example.imccalculator.model.bmi.BMI
import kotlinx.coroutines.flow.Flow

interface BMIRepository {
    suspend fun insert(
        name: String,
        age: Int,
        gender: String,
        weight: Double,
        height: Double,
        bmi : Double,
        bmiClass: String,
        tmb: Double,
        idealWeight: Double,
        dailyCalories: Double,
        activityLevelFactor: Double,
        timestamp: Long
    )

    suspend fun deleteAll()

    suspend fun delete(bmi: BMI)


    fun getAll(): Flow<List<BMI>>

    suspend fun getById(id: Long): BMI
}