package com.example.imccalculator.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.imccalculator.model.bmi.BMI
import com.example.imccalculator.ui.theme.IMCCalculatorTheme
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BMIItem(
    bmiItem: BMI,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
){

    val formattedDate = remember(bmiItem.timestamp){
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        formatter.format(Date(bmiItem.timestamp))
    }

    Surface(
        onClick = onItemClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.small
    ){
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = onCheckedChange
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ){
                    Text(
                        text = bmiItem.name,
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "IMC: ${"%.2f".format(bmiItem.bmi)}",
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = bmiItem.bmiClass,
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Text(
                    text = formattedDate,
                    fontSize = 12.sp,
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview
@Composable
fun BMIItemPreview(){
    IMCCalculatorTheme{
        BMIItem(
            bmiItem = BMI(
               id = 1,
                name = "Eduardo",
                age = 20,
                gender = "male",
                weight = 80.2,
                height = 120.0,
                bmi = 24.0,
                bmiClass = "Sobrepeso",
                tmb = 1850.0,
                idealWeight = 75.0,
                dailyCalories = 2200.0,
                activityLevelFactor = 0.0,
                timestamp = System.currentTimeMillis()
            ),
            onItemClick = {},
            isChecked = false,
            onCheckedChange = {}
        )
    }
}