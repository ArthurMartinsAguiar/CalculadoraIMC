package com.example.imccalculator.ui.view.list

interface ListEvent {
    data object OnDeleteBMIsClick: ListEvent
    data object OnAddBMIClick: ListEvent
    data class OnBMIItemClick(val id: Long): ListEvent

    data class OnBMIItemCheckedChange(val id: Long, val isChecked: Boolean): ListEvent

}