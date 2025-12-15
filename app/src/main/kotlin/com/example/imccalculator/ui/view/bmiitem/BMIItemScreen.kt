package com.example.imccalculator.ui.view.bmiitem

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.imccalculator.data.bmi.BMIDatabaseProvider
import com.example.imccalculator.data.bmi.BMIRepositoryImpl
import com.example.imccalculator.model.bmi.BMI
import com.example.imccalculator.ui.UiEvent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BMIItemScreen(
    id: Long,
    navigateBack: () -> Unit
){
    val context = LocalContext.current.applicationContext
    val database = BMIDatabaseProvider.provide(context)
    val repository = BMIRepositoryImpl(database.bmiDao)
    val viewModel = viewModel<BMIItemViewModel> { BMIItemViewModel(id, repository) }
    val state by viewModel.uiState.collectAsState()
    val activityLevelList = viewModel.activityLevelList

        LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is UiEvent.NavigateBack -> { navigateBack() }
                is UiEvent.ShowSnackbar -> {}
                is UiEvent.Navigate<*> -> {}
            }
        }
    }

    when(val uiState = state) {
        BMIItemUiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is BMIItemUiState.Success -> {
            BMIItemContent(
                bmiItem = uiState.bmiItem,
                onEvent = viewModel::onEvent,
                onBack = navigateBack,
                activityLevelList = activityLevelList
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BMIItemContent(
    bmiItem: BMI,
    onEvent: (BMIItemEvent) -> Unit,
    onBack: () -> Unit,
    activityLevelList: List<Pair<String, Double>>
){
    // Formatação da Data
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
    val formattedDate = dateFormatter.format(Date(bmiItem.timestamp))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes da Medição") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(BMIItemEvent.OnDone )}) {
                Icon(Icons.Default.Done, contentDescription = "Concluir")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .consumeWindowInsets(innerPadding)
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()), // Permite rolar a tela se for pequena
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 1. Cabeçalho (Nome e Data)
            Text(
                text = bmiItem.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // 2. Grid de Informações Pessoais
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                InfoItem(label = "Idade", value = "${bmiItem.age} anos")
                InfoItem(label = "Sexo", value = if (bmiItem.gender == "male") "Masc." else "Fem.")
                InfoItem(label = "Altura", value = "${bmiItem.height} cm")
                InfoItem(label = "Peso", value = "${bmiItem.weight} kg")
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))

            // 3. Destaque do IMC
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("IMC (Índice de Massa Corporal)", style = MaterialTheme.typography.labelLarge)
                    Text(
                        text = "%.2f".format(bmiItem.bmi),
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = bmiItem.bmiClass,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Indicadores Adicionais",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 4. Cards dos Novos Cálculos

            // TMB
            DetailCard(
                title = "Taxa Metabólica Basal (TMB)",
                value = "%.0f kcal".format(bmiItem.tmb),
                description = "Gasto calórico em repouso absoluto."
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Peso Ideal
            DetailCard(
                title = "Peso Ideal Estimado",
                value = "%.1f kg".format(bmiItem.idealWeight),
                description = "Baseado na fórmula de Devine."
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Calorias Diárias
            DetailCard(
                title = "Necessidade Calórica Diária",
                value = "%.0f kcal".format(bmiItem.dailyCalories),
                description = "Para manter o peso (Considerando nível de atividade: ${
                    activityLevelList.find { it.second == bmiItem.activityLevelFactor }?.first
                })."
            )

            // Espaço extra para o botão flutuante não cobrir o texto
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

// Componente auxiliar para os itens pequenos (Idade, Sexo, etc)
@Composable
fun InfoItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
    }
}

// Componente auxiliar para os cards de resultado
@Composable
fun DetailCard(title: String, value: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, style = MaterialTheme.typography.labelLarge)
                Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}