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
import androidx.navigation.navArgument
import androidx.navigation.NavType

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
    val authViewModel: AuthViewModel = viewModel()
    NavHost(navController = navController, startDestination = "auth") {

        // AuthScreen
        composable("auth") {
            AuthScreen(navController = navController, authViewModel = authViewModel)
        }

        // MainMenuPage
        composable("menu") {
            MainMenuPage(
                onNavigate = { page -> navController.navigate(page) },
                navController = navController,
                authViewModel = authViewModel
            )
        }

        // CameraPage
        composable("camera") { CameraPage(
            onNavigate = { page -> navController.navigate(page) },
        ) }

        // SearchFoodPage
        composable("pesquisarAlimentos") {
            SearchFoodPage(
                onBackClick = { navController.popBackStack() },
                navController = navController,
                onCameraClick = { navController.navigate("camera")}

            )
        }

        // ScanBarcodePage
        composable("lerCodigoBarras") {
            ScanBarcodePage(onBackClick = { navController.popBackStack() })
        }

        // RecipeGenPage
        composable("gerarReceitas") { RecipeGenPage(
            onBackClick = { navController.popBackStack() },
            navController= navController) }

        // RecipeDetailsPage
        composable(
            route = "recipeDetails/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt("recipeId")
            recipeId?.let {
                RecipeDetailsPage(
                    recipeId = it,
                    onBackClick = { navController.popBackStack() },
                    navController = navController
                )
            }
        }

        // IngredientInformationPage
        composable(
            route = "ingredientInformation/{ingredientId}/{ingredientName}",
            arguments = listOf(
                navArgument("ingredientId") { type = NavType.IntType },
                navArgument("ingredientName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val ingredientId = backStackEntry.arguments?.getInt("ingredientId") ?: 0
            val ingredientName = backStackEntry.arguments?.getString("ingredientName").orEmpty()

            IngredientInformationPage(
                ingredientId = ingredientId,
                ingredientName = ingredientName,
                onBackClick = { navController.popBackStack() },
                navController = navController
            )
        }
        // HistoryPage
        composable("historicoReceitas") {
            HistoryPage(
                onBackClick = { navController.popBackStack() },
                navController = navController)
        }
    }
}
