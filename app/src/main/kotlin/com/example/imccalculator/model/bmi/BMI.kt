package com.example.imccalculator.model.bmi

data class BMI(
    val id: Long,
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