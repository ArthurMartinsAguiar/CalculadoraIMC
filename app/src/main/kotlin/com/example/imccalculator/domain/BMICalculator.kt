package com.example.imccalculator.domain

import com.example.imccalculator.model.bmi.BMICalculationResult
import com.example.imccalculator.model.bmi.BMIResult

object BMICalculator{

    fun calculateBMI(weight: Double, height: Double): BMIResult{
        val heightMeters = height/100.0
        val bmi = weight / (heightMeters * heightMeters)
        val classification = bmiClassification(bmi)
        return BMIResult(bmi, classification)
    }

    private fun bmiClassification(bmi: Double): String{
        var classification = ""
        if(bmi < 18.5) classification = "Magreza"
        if(bmi in 18.5..<25.0) classification = "Normal"
        if(bmi in 25.0..<30.0) classification = "Sobrepeso"
        if(bmi in 30.0..<40.0) classification = "Obesidade"
        if(bmi >= 40) classification = "Obesidade grave"
        return classification
    }

    fun calculateComplete(
        weight: Double,
        height: Double,
        activityLevelFactor: Double,
        age: Int,
        gender: String
    ): BMICalculationResult {

        //reutiliza  calculo do imc
        val bmiBasic = calculateBMI(weight, height)
        //calcuila tmb
        val tmb = calculateTMB(weight, height, age, gender)
        //calcula peso ideal
        val idealWeight = calculateIdealWeight(height, gender)

        // calcula calorias (TMB * Fator sedentario 1.2 como padrão)
        val dailyCalories = tmb * activityLevelFactor

        return BMICalculationResult(
            bmi = bmiBasic.bmi,
            bmiClass = bmiBasic.bmiClass,
            tmb = tmb,
            idealWeight = idealWeight,
            dailyCalories = dailyCalories
        )
    }
    private fun calculateTMB(weight: Double, height: Double, age: Int, gender: String): Double {
        return if (gender == "male") {
            88.36 + (13.4 * weight) + (4.8 * height) - (5.7 * age)
        } else {
            447.6 + (9.2 * weight) + (3.1 * height) - (4.3 * age)
        }
    }

    private fun calculateIdealWeight(heightCm: Double, gender: String): Double {
        // Altura base = 5 pés = 152.4 cm
        // Para cada polegada (2.54 cm) acima disso, soma-se um valor
        if (heightCm <= 152.4) return if (gender == "male") 50.0 else 45.5

        val heightOver = heightCm - 152.4
        val inchesOver = heightOver / 2.54

        return if (gender == "male") {
            50.0 + (2.3 * inchesOver)
        } else {
            45.5 + (2.3 * inchesOver)
        }
    }

}