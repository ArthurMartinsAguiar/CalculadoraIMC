package com.example.imccalculator.ui.view.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imccalculator.data.bmi.BMIRepository
import com.example.imccalculator.navigation.AddBMIRoute
import com.example.imccalculator.navigation.BMIItemRoute
import com.example.imccalculator.ui.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ListViewModel(
    private val repository : BMIRepository
) : ViewModel() {

    val bmiItems = repository.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _selectedIds = MutableStateFlow<Set<Long>>(emptySet())
    val selectedIds = _selectedIds.asStateFlow()
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: ListEvent){
        when(event){
            // navegação para adicionar

            is ListEvent.OnAddBMIClick -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.Navigate(AddBMIRoute))
                }
            }
            // navegação para detalhes
            is ListEvent.OnBMIItemClick -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.Navigate(BMIItemRoute(event.id)))
                }
            }
            is ListEvent.OnBMIItemCheckedChange -> {
                val currentIds = _selectedIds.value.toMutableSet()
                if (event.isChecked){
                    currentIds.add(event.id)
                }else{
                    currentIds.remove(event.id)
                }
                _selectedIds.value = currentIds
            }

            is ListEvent.OnDeleteBMIsClick -> {
                viewModelScope.launch {
                    val idsToDelete = _selectedIds.value
                    if (idsToDelete.isNotEmpty()){
                        val itemsToDelete = bmiItems.value.filter {it.id in idsToDelete}
                        itemsToDelete.forEach { bmi ->
                            repository.delete(bmi)
                        }
                        _selectedIds.value = emptySet()
                    }
                }
            }

        }
    }
}