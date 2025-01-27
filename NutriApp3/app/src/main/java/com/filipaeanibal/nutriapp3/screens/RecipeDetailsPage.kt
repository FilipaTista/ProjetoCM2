@file:OptIn(ExperimentalMaterial3Api::class)

package com.filipaeanibal.nutriapp3.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.filipaeanibal.nutriapp3.util.NetworkResult
import com.filipaeanibal.nutriapp3.util.RecipeDetailsViewModel
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlin.math.roundToInt
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.filipaeanibal.nutriapp3.models.RecipeInstructions.RecipeInstructions


@Composable
fun RecipeDetailsPage(
    recipeId: Int,
    onBackClick: () -> Unit,
    viewModel: RecipeDetailsViewModel = hiltViewModel(),
    navController: NavHostController
) {
    // Trigger fetch when the page loads
    LaunchedEffect(recipeId) {
        viewModel.fetchRecipeDetails(recipeId)
        viewModel.fetchRecipeInstructions(recipeId)
    }

    val recipeDetailsState by viewModel.recipeDetails.collectAsState()

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Text(
                        text = "Detalhes da Receita",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = recipeDetailsState) {
                is NetworkResult.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is NetworkResult.Success -> {
                    val recipeDetails = state.data
                    LazyColumn(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            // Main Recipe Card similar to RecipeGenPage
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(8.dp)
                            ) {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    // Background Image
                                    recipeDetails.image?.let {
                                        AsyncImage(
                                            model = it,
                                            contentDescription = recipeDetails.title,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    // Gradient Overlay
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                Brush.verticalGradient(
                                                    colors = listOf(
                                                        Color.Transparent,
                                                        Color.Black.copy(alpha = 0.8f)
                                                    )
                                                )
                                            )
                                    )

                                    // Recipe Details
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(16.dp),
                                        verticalArrangement = Arrangement.Bottom
                                    ) {
                                        Text(
                                            text = recipeDetails.title,
                                            style = MaterialTheme.typography.headlineSmall,
                                            color = Color.White,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Row(
                                            modifier = Modifier.padding(top = 8.dp),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            // Time Chip
                                            RecipeChip(
                                                icon = {
                                                    Icon(
                                                        Icons.Outlined.Timer,
                                                        contentDescription = "Tempo de preparo",
                                                        tint = Color.White
                                                    )
                                                },
                                                label = {
                                                    Text(
                                                        "${recipeDetails.readyInMinutes} min",
                                                        color = Color.White
                                                    )
                                                },
                                            )

                                            // Servings Chip
                                            RecipeChip(
                                                icon = {
                                                    Icon(
                                                        Icons.Outlined.People,
                                                        contentDescription = "Porções",
                                                        tint = Color.White
                                                    )
                                                },
                                                label = {
                                                    Text(
                                                        "${recipeDetails.servings} porções",
                                                        color = Color.White
                                                    )
                                                },
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Nutrition Information Section
                        item {
                            Text(
                                text = "Nutritional value per serving",
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        // Caloric Breakdown
                        item {
                            recipeDetails.nutrition?.caloricBreakdown?.let { breakdown ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = "Caloric Distribution",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Text("Proteins: ${breakdown.percentProtein.roundToInt()}%")
                                        Text("Carbohydrates: ${breakdown.percentCarbs.roundToInt()}%")
                                        Text("Fats: ${breakdown.percentFat.roundToInt()}%")
                                    }
                                }
                            }
                        }

                        // Key Nutrients
                        item {
                            recipeDetails.nutrition?.nutrients?.let { nutrients ->
                                // Definir a ordem desejada
                                val nutrientOrder = listOf("Calories", "Protein", "Carbohydrates", "Fat")

                                // Filtrar e ordenar os nutrientes
                                val mainNutrients = nutrients.filter { nutrient ->
                                    nutrient.name in nutrientOrder
                                }.sortedBy { nutrient ->
                                    nutrientOrder.indexOf(nutrient.name)
                                }
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = "Nutritional Information",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        // Exibir os nutrientes filtrados e ordenados
                                        mainNutrients.forEach { nutrient ->
                                            Text("${nutrient.name}: ${nutrient.amount.roundToInt()} ${nutrient.unit}")
                                        }
                                    }
                                }
                            }
                        }
                        item {
                            when (val instructionsState = viewModel.recipeInstructions.collectAsState().value) {
                                is NetworkResult.Success -> {
                                    instructionsState.data?.let { instructions ->
                                        // Renderiza ingredientes diretamente no escopo do LazyColumn
                                        IngredientsSection(
                                            instructions = instructions,
                                            onIngredientClick = { ingredientId ->
                                                navController.navigate("ingredientInformation/$ingredientId")
                                            }
                                        )
                                    }
                                }
                                is NetworkResult.Loading -> {
                                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                                }
                                is NetworkResult.Error -> {
                                    Text(
                                        text = instructionsState.message ?: "Erro ao carregar instruções",
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                        item {
                            when (val instructionsState = viewModel.recipeInstructions.collectAsState().value) {
                                is NetworkResult.Success -> {
                                    instructionsState.data?.let { instructions ->
                                        InstructionsSection(instructions)
                                    }
                                }
                                is NetworkResult.Loading -> {
                                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                                }
                                is NetworkResult.Error -> {
                                    Text(
                                        text = instructionsState.message ?: "Erro ao carregar instruções",
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
                is NetworkResult.Error -> {
                    Text(
                        text = state.message ?: "Erro desconhecido",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun InstructionsSection(instructions: RecipeInstructions) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Instruções",
                style = MaterialTheme.typography.titleLarge
            )

            instructions.forEach { instructionItem ->
                instructionItem.steps.forEach { step ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "${step.number}.",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.width(32.dp)
                        )
                        Text(step.step)

                    }
                }
            }
        }
    }
}

@Composable
fun IngredientsSection(
    instructions: RecipeInstructions,
    onIngredientClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Ingredientes",
                style = MaterialTheme.typography.titleLarge
            )

            val uniqueIngredients = instructions
                .flatMap { it.steps }
                .flatMap { it.ingredients }
                .distinctBy { it.id }

            if (uniqueIngredients.isEmpty()) {
                Text(
                    text = "Nenhum ingrediente encontrado.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                // Renderiza ingredientes diretamente
                uniqueIngredients.forEach { ingredient ->
                    Text(
                        text = ingredient.name,
                        modifier = Modifier.clickable { onIngredientClick(ingredient.id) },
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}


