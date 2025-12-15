package com.example.imccalculator.ui.view.bmiitem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imccalculator.data.bmi.BMIRepository
import com.example.imccalculator.domain.activityLevelList
import com.example.imccalculator.ui.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class BMIItemViewModel(
    private val id: Long,
    private val repository : BMIRepository
): ViewModel(){

    private val _uiState = MutableStateFlow<BMIItemUiState> (BMIItemUiState.Loading)
    val uiState: StateFlow<BMIItemUiState> = _uiState
    val activityLevelList = activityLevelList()

    init {
        viewModelScope.launch {
            val bmiItem = repository.getById(id)
            _uiState.value = BMIItemUiState.Success(bmiItem)
        }
    }
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: BMIItemEvent){
        when(event){
            is BMIItemEvent.OnDone -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.NavigateBack)
                }
            }
        }
    }

}