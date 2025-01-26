package com.filipaeanibal.nutriapp3

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.filipaeanibal.nutriapp3.screens.*

@Composable
fun NutriApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "auth") {
        composable("auth") { AuthScreen(navController = navController) }
        //composable("login") { LoginPage(onLoginSuccess = { navController.navigate("menu") }) }
        composable("menu") { MainMenuPage(onNavigate = { page -> navController.navigate(page) }) }
        composable("detectarObjetos") {
            DetectObjectsPage(
                onCameraClick = { navController.navigate("camera") },
                onBarcodeClick = { navController.navigate("lerCodigoBarras") },
                onSearchClick = { navController.navigate("pesquisarAlimentos") }
            )
        }
        composable("camera") { CameraPage() }
        composable("pesquisarAlimentos") {
            SearchFoodPage(onBackClick = { navController.popBackStack() })
        }
        composable("lerCodigoBarras") {
            ScanBarcodePage(onBackClick = { navController.popBackStack() })
        }
        composable("gerarReceitas") { RecipeGenPage(navController) }

        composable(
            route = "recipeDetails/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt("recipeId")
            recipeId?.let {
                RecipeDetailsPage(
                    recipeId = it,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
        composable("historicoReceitas") { HistoryPage() }

    }
}
