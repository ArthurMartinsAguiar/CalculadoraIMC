package com.example.imccalculator.ui.view.bmiitem

import com.example.imccalculator.model.bmi.BMI

sealed interface BMIItemUiState {
    object Loading: BMIItemUiState
    data class Success(val bmiItem: BMI) : BMIItemUiState
}