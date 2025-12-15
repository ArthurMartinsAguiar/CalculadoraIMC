package com.example.imccalculator.ui.view.addbmi

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.imccalculator.data.bmi.BMIDatabaseProvider
import com.example.imccalculator.data.bmi.BMIRepositoryImpl
import com.example.imccalculator.ui.UiEvent
import com.example.imccalculator.ui.theme.IMCCalculatorTheme

@Composable
fun AddBMIScreen(
    navigateBack: () -> Unit
){
    val context = LocalContext.current.applicationContext
    val database = BMIDatabaseProvider.provide(context)
    val repository = BMIRepositoryImpl(database.bmiDao)
    val viewModel = viewModel<AddBMIViewModel> { AddBMIViewModel(repository) }
    val name = viewModel.name
    val age = viewModel.age
    val gender = viewModel.gender
    val height = viewModel.height
    val weight = viewModel.weight
    val activityLevelList = viewModel.activityLevelList
    val activityLevelFactor = viewModel.activityLevelFactor
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(uiEvent.message)
                }
                is UiEvent.NavigateBack -> { navigateBack() }
                is UiEvent.Navigate<*> -> {}
            }
        }
    }

    AddBMIContent(
        name = name,
        age = age,
        gender = gender,
        height = height,
        weight = weight,
        activityLevelList = activityLevelList,
        activityLevelFactor = activityLevelFactor,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
        onBack = navigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBMIContent(
    name: String,
    age: String,
    gender: String,
    height: String,
    weight: String,
    activityLevelList: List<Pair<String, Double>>,
    activityLevelFactor: Double,
    snackbarHostState: SnackbarHostState,
    onEvent: (AddBMIEvent) -> Unit,
    onBack: () -> Unit
){
    var expanded by remember { mutableStateOf(false) }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Adicionar IMC") },
                navigationIcon = {
                    IconButton(onClick = onBack){
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    )
    {
        innerPadding ->
        Column(
            Modifier
                .consumeWindowInsets(innerPadding)
                .padding(innerPadding)
                .padding(16.dp)

        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = { onEvent(AddBMIEvent.OnNameChange(it)) },
                placeholder = { Text("Nome da pessoa") },
                label = {Text("Nome")},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Words
                ),
                isError = name.isBlank()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- SEXO (Radio Buttons) ---
            Text(text = "Sexo:", style = MaterialTheme.typography.bodyLarge)
            Row(Modifier.fillMaxWidth()) {
                // Opção Masculino
                Row(
                    Modifier
                        .weight(1f)
                        .height(56.dp)
                        .selectable(
                            selected = (gender == "male"),
                            onClick = { onEvent(AddBMIEvent.OnGenderChange("male")) },
                            role = Role.RadioButton
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (gender == "male"),
                        onClick = null // null porque o clique é controlado pelo Row
                    )
                    Text(text = "Masculino", modifier = Modifier.padding(start = 8.dp))
                }

                // Opção Feminino
                Row(
                    Modifier
                        .weight(1f)
                        .height(56.dp)
                        .selectable(
                            selected = (gender == "female"),
                            onClick = { onEvent(AddBMIEvent.OnGenderChange("female")) },
                            role = Role.RadioButton
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (gender == "female"),
                        onClick = null
                    )
                    Text(text = "Feminino", modifier = Modifier.padding(start = 8.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = age,
                onValueChange = { onEvent(AddBMIEvent.OnAgeChange(it)) },
                label = { Text("Idade (anos)") },
                placeholder = { Text("Ex: 25") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
            )

            Spacer(modifier = Modifier.height(16.dp))

            /*Peso*/
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = height,
                onValueChange = { onEvent(AddBMIEvent.OnHeightChange(it)) },
                placeholder = { Text("Altura (cm)...") },
                label = {Text ("Altura")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                isError = height.isBlank() && height.toDoubleOrNull() == null
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = weight,
                onValueChange = { onEvent(AddBMIEvent.OnWeightChange(it)) },
                placeholder = { Text("Peso (kg)...") },
                label = { Text("Peso (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = weight.isBlank() && weight.toDoubleOrNull() == null
            )

            Spacer(modifier = Modifier.height(16.dp))
            //Nivel de atividade

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {expanded = !expanded}
            ) {
                OutlinedTextField(
                    value = activityLevelList.find {it.second == activityLevelFactor}?.first ?: "Selecione",
                    onValueChange = {},
                    readOnly = true,
                    label = {Text("Nivel de atividade")},
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable, enabled = true)
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                activityLevelList.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.first) },
                        onClick = {
                            onEvent(AddBMIEvent.OnActivityLevelChange(option.second))
                            expanded = false
                        })
                    }
                }
            }

            Button(
                onClick = { onEvent(AddBMIEvent.OnSave) },
                modifier = Modifier.fillMaxWidth().height(70.dp).padding(10.dp)
            ) { Text(text = "Calcular", fontSize = 20.sp) }
        }
    }
}

@Preview
@Composable
fun AddBMIScreenPreview(){
    IMCCalculatorTheme {
        AddBMIContent(
            name = "",
            age = "",
            gender = "male",
            height = "",
            weight = "",
            snackbarHostState = SnackbarHostState(),
            activityLevelList = emptyList(),
            activityLevelFactor = 0.0,
            onEvent = {},
            onBack = {}
        )
    }
}
