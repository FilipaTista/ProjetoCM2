package com.filipaeanibal.nutriapp3.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.filipaeanibal.nutriapp3.util.IngredientInformationViewModel
import com.filipaeanibal.nutriapp3.util.NetworkResult
import androidx.compose.material3.Text
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import kotlin.math.roundToInt
import androidx.compose.ui.graphics.Color
import com.filipaeanibal.nutriapp3.models.SearchRecipesbyIngredients.SearchRecipesbyIngredientsItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientInformationPage(
    ingredientId: Int,
    ingredientName: String,
    onBackClick: () -> Unit,
    viewModel: IngredientInformationViewModel = hiltViewModel()
) {
    LaunchedEffect(ingredientId) {
        viewModel.fetchIngredientInformation(ingredientId)
        viewModel.fetchRecipesByIngredient(ingredientName)
    }

    val ingredientState by viewModel.ingredientInformation.collectAsState()
    val recipebyIngredientState by viewModel.recipesByIngredient.collectAsState()

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
                        text = "Detalhes do Ingrediente",
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
            LazyColumn(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (val state = ingredientState) {
                    is NetworkResult.Loading -> {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                    is NetworkResult.Success -> {
                        val ingredient = state.data
                        // Nome do ingrediente
                        item {
                            Text(
                                text = ingredient.name,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        // Informação Nutricional
                        item {
                            ingredient.nutrition?.nutrients?.let { nutrients ->
                                val nutrientOrder = listOf("Calories", "Protein", "Carbohydrates", "Fat")
                                val mainNutrients = nutrients.filter { it.name in nutrientOrder }
                                    .sortedBy { nutrientOrder.indexOf(it.name) }

                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = "Informação Nutricional",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        mainNutrients.forEach { nutrient ->
                                            Text("${nutrient.name}: ${nutrient.amount.roundToInt()} ${nutrient.unit}")
                                        }
                                    }
                                }
                            }
                        }

                        // Receitas relacionadas
                        item {
                            Text(
                                text = "Receitas com este Ingrediente",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                    is NetworkResult.Error -> {
                        item {
                            Text(
                                text = state.message ?: "Erro ao carregar detalhes do ingrediente.",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }

                // Receitas
                item {
                    when (val state = recipebyIngredientState) {
                        is NetworkResult.Loading -> {
                            CircularProgressIndicator()
                        }
                        is NetworkResult.Success -> {
                            val recipes = state.data.orEmpty()
                            if (recipes.isEmpty()) {
                                Text(
                                    text = "Nenhuma receita encontrada.",
                                    color = MaterialTheme.colorScheme.error
                                )
                            } else {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(recipes) { recipe ->
                                        RecipeCardByIngredient(recipe)
                                    }
                                }
                            }
                        }
                        is NetworkResult.Error -> {
                            Text(
                                text = state.message ?: "Erro ao carregar receitas.",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun RecipeCardByIngredient(recipe: SearchRecipesbyIngredientsItem) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(150.dp)
            .clickable { /* Ação ao clicar no card */ },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = recipe.image,
                contentDescription = recipe.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}