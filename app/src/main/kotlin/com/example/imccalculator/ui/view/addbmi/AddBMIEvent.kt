package com.example.imccalculator.ui.view.addbmi

interface AddBMIEvent {
    data class OnNameChange (val name: String): AddBMIEvent
    data class OnAgeChange (val age: String): AddBMIEvent
    data class OnGenderChange (val gender: String): AddBMIEvent
    data class OnWeightChange(val weight: String): AddBMIEvent
    data class OnHeightChange(val height: String): AddBMIEvent
    data class OnActivityLevelChange(val activityLevelFactor: Double): AddBMIEvent
    data object OnSave: AddBMIEvent
}