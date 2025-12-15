package com.example.imccalculator.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.imccalculator.ui.view.addbmi.AddBMIScreen
import com.example.imccalculator.ui.view.bmiitem.BMIItemScreen
import com.example.imccalculator.ui.view.list.ListScreen
import kotlinx.serialization.Serializable

@Serializable
object ListRoute

@Serializable
object AddBMIRoute

@Serializable
data class BMIItemRoute(val id: Long)

@Composable
fun BMINavHost(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ListRoute){
        composable<ListRoute> {
            ListScreen(
                navigateToAddBMIScreen = { navController.navigate(AddBMIRoute) },
                navigateToBMIItemScreen = { id -> navController.navigate(BMIItemRoute(id)) }
            )
        }

        composable <AddBMIRoute> {
            AddBMIScreen(
                navigateBack = { navController.popBackStack() }
            )
        }
        
        composable <BMIItemRoute> {
            backStackEntry -> val bmiItemRoute = backStackEntry.toRoute<BMIItemRoute>()
                BMIItemScreen(
                    id = bmiItemRoute.id,
                    navigateBack = { navController.popBackStack() }
                )
        }
    }
}