package com.filipaeanibal.nutriapp3

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.filipaeanibal.nutriapp3.screens.*
import com.filipaeanibal.nutriapp3.util.AuthViewModel

@Composable
fun NutriApp() {
    val navController = rememberNavController()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        AppNavGraph(navController = navController)
    }
}
@Composable
fun AppNavGraph(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel() // Se estiver a usar Hilt
    NavHost(navController = navController, startDestination = "auth") {
        composable("auth") {
            AuthScreen(navController = navController, authViewModel = authViewModel)
        }
        composable("menu") {
            MainMenuPage(
                onNavigate = { page -> navController.navigate(page) },
                navController = navController, // Passar o navController corretamente aqui
                authViewModel = authViewModel
            )
        }
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
