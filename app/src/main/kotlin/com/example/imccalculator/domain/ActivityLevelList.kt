package com.example.imccalculator.domain
fun activityLevelList(): List<Pair<String, Double>> {
    return listOf(
        "Sedentário" to 1.2,
        "Leve (1-3 dias)" to 1.375,
        "Moderado (3-5 dias)" to 1.55,
        "Ativo (6-7 dias)" to 1.725
    )
}

