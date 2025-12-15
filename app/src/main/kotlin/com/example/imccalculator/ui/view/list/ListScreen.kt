package com.example.imccalculator.ui.view.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection // Importante para o padding
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.imccalculator.data.bmi.BMIDatabaseProvider
import com.example.imccalculator.data.bmi.BMIRepositoryImpl
import com.example.imccalculator.model.bmi.BMI
import com.example.imccalculator.navigation.AddBMIRoute
import com.example.imccalculator.navigation.BMIItemRoute
import com.example.imccalculator.ui.UiEvent
import com.example.imccalculator.ui.components.BMIItem

/*Tela gerada para toda medição e acessos pelo histórico*/
@Composable
fun ListScreen(
    navigateToAddBMIScreen: () -> Unit,
    navigateToBMIItemScreen: (id: Long) -> Unit
){
    val context = LocalContext.current.applicationContext
    val database = BMIDatabaseProvider.provide(context)
    val repository = BMIRepositoryImpl(database.bmiDao)
    val viewModel = viewModel<ListViewModel> {
        ListViewModel(repository)
    }
    val bmiItems by viewModel.bmiItems.collectAsState()
    val selectedIds by viewModel.selectedIds.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is UiEvent.Navigate<*> -> {
                    when (uiEvent.route) {
                        is AddBMIRoute -> { navigateToAddBMIScreen() }
                        is BMIItemRoute ->{ navigateToBMIItemScreen(uiEvent.route.id) }
                    }
                }
                is UiEvent.NavigateBack -> {}
                is UiEvent.ShowSnackbar -> {}
            }
        }
    }

    ListContent(
        bmiItems = bmiItems,
        selectedIds = selectedIds,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun ListContent(
    bmiItems: List<BMI>,
    selectedIds: Set<Long> = emptySet(),
    onEvent: (ListEvent) -> Unit
){
    Scaffold(
        floatingActionButton = {
            Row {
                FloatingActionButton(onClick = { onEvent(ListEvent.OnDeleteBMIsClick) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }

                Spacer(modifier = Modifier.width(16.dp))

                FloatingActionButton(onClick = { onEvent(ListEvent.OnAddBMIClick) }) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }
    ) { innerPadding ->

        // CORREÇÃO 1: Obter a direção do layout para calcular padding start/end
        val layoutDirection = LocalLayoutDirection.current

        LazyColumn(
            modifier = Modifier
                .consumeWindowInsets(innerPadding)
                .fillMaxWidth(),
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding() + 16.dp,
                bottom = innerPadding.calculateBottomPadding() + 16.dp,
                // Agora 'layoutDirection' existe
                start = innerPadding.calculateStartPadding(layoutDirection) + 16.dp,
                end = innerPadding.calculateEndPadding(layoutDirection) + 16.dp
            )
        ) {
            itemsIndexed(bmiItems){ index, bmiItem ->

                // CORREÇÃO 2: Lógica real de seleção
                val isSelected = selectedIds.contains(bmiItem.id)

                BMIItem(
                    bmiItem = bmiItem,
                    // CORREÇÃO 3: Sintaxe correta. onItemClick fecha chaves, vírgula, depois vem os outros.
                    onItemClick = {
                        onEvent(ListEvent.OnBMIItemClick(bmiItem.id))
                    },
                    isChecked = isSelected,
                    onCheckedChange = { isChecked ->
                        onEvent(ListEvent.OnBMIItemCheckedChange(bmiItem.id, isChecked))
                    }
                )
		
		        if(index < bmiItems.lastIndex) {
			        Spacer(modifier = Modifier.height(8.dp))
		        }
            }
        }
    }
}

@Preview
@Composable
fun ListContentPreview(){
    // ListContent(bmis = listOf(BMI(
    //    1, 80.2, 120.0, 24.0, "Sobrepeso", System.currentTimeMillis()
   // )))
}
