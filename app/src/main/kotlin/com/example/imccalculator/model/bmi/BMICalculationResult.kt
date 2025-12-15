package com.example.imccalculator.model.bmi

data class BMICalculationResult(
    val bmi: Double,
    val bmiClass: String,
    val tmb: Double,
    val idealWeight: Double,
    val dailyCalories: Double
)
