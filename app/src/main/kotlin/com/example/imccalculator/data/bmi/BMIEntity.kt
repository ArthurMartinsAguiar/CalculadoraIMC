package com.example.imccalculator.data.bmi

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bmis")
data class BMIEntity (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val age: Int,
    val gender: String,
    val weight: Double,
    val height: Double,
    val bmi : Double,
    val bmiClass: String,
    val tmb: Double,
    val idealWeight: Double,
    val dailyCalories: Double,
    val activityLevelFactor: Double,
    val timestamp: Long
)