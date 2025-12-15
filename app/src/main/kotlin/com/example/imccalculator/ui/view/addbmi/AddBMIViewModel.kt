package com.example.imccalculator.ui.view.addbmi

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imccalculator.data.bmi.BMIRepository
import com.example.imccalculator.domain.BMICalculator
import com.example.imccalculator.domain.activityLevelList
import com.example.imccalculator.ui.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddBMIViewModel(
    private val repository : BMIRepository
): ViewModel() {

    var name by mutableStateOf("")
        private set

    var age by mutableStateOf("")
        private set

    var gender by mutableStateOf("male")
        private set

    var weight by mutableStateOf("")
        private set


    var height by mutableStateOf("")
        private set

    val activityLevelList = activityLevelList()

    var activityLevelFactor by mutableDoubleStateOf(0.0)
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event : AddBMIEvent) {
        when (event) {
            is AddBMIEvent.OnNameChange -> {
                name = event.name
            }

            is AddBMIEvent.OnAgeChange -> {
                age = event.age
            }

            is AddBMIEvent.OnGenderChange -> {
                gender = event.gender
            }

            is AddBMIEvent.OnHeightChange -> {
                height = event.height
            }

            is AddBMIEvent.OnWeightChange -> {
                weight = event.weight
            }

            is AddBMIEvent.OnActivityLevelChange -> {
                activityLevelFactor = event.activityLevelFactor
            }

            is AddBMIEvent.OnSave -> {
                val nameValue = name
                val heightValue = height.toDoubleOrNull()
                val weightValue = weight.toDoubleOrNull()
                val ageValue = age.toIntOrNull()
                val activityLevelFactorValue = activityLevelFactor

                if(nameValue.isBlank()){
                    sendUiEvent(UiEvent.ShowSnackbar("Por favor, insira um nome!"))
                    return
                }

                if(ageValue == null || ageValue <= 0){
                    sendUiEvent(UiEvent.ShowSnackbar("Por favor, insira uma idade válida!"))
                    return
                }

                if (heightValue == null || weightValue == null) {
                    sendUiEvent(UiEvent.ShowSnackbar("Por favor, insira valores válidos!"))
                    return
                }

                if (heightValue <= 0 || weightValue <= 0) {
                    sendUiEvent(UiEvent.ShowSnackbar("Por favor, insira valores positivos!"))
                    return
                }

                if(activityLevelFactorValue == 0.0){
                    sendUiEvent(UiEvent.ShowSnackbar("Por favor, selecione um nível de atividade!"))
                    return
                }

                saveBMI(name, ageValue, gender, weightValue, heightValue)
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch{
            _uiEvent.send(event)
        }
    }


    private fun saveBMI(name: String,age: Int, gender:String, weightValue: Double, heightValue: Double){
        viewModelScope.launch {
            val result = BMICalculator.calculateComplete(
                weight = weightValue,
                height = heightValue,
                activityLevelFactor = activityLevelFactor,
                age = age,
                gender = gender,
            )

            repository.insert(
                name = name,
                age = age,
                gender = gender,
                weight = weightValue,
                height = heightValue,
                bmi = result.bmi,
                bmiClass = result.bmiClass,
                tmb = result.tmb,
                idealWeight = result.idealWeight,
                dailyCalories = result.dailyCalories,
                activityLevelFactor = activityLevelFactor,
                timestamp = System.currentTimeMillis()

            )
            sendUiEvent(UiEvent.NavigateBack)
        }
    }
}