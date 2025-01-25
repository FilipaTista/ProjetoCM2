package com.filipaeanibal.nutriapp3

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.filipaeanibal.nutriapp3.screens.*

@Composable
fun NutriApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginPage(onLogin = { navController.navigate("menu") }) }
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
        composable("gerarReceitas") { RecipeGenPage() }
        composable("historicoReceitas") { HistoryPage() }
    }
}
