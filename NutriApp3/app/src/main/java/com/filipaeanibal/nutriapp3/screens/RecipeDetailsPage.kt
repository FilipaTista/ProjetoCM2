@file:OptIn(ExperimentalMaterial3Api::class)

package com.filipaeanibal.nutriapp3.screens

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.filipaeanibal.nutriapp3.util.NetworkResult
import com.filipaeanibal.nutriapp3.util.RecipeDetails.RecipeDetailsViewModel
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.filipaeanibal.nutriapp3.components.NutrientColors
import com.filipaeanibal.nutriapp3.components.NutrientData
import com.filipaeanibal.nutriapp3.components.NutrientPieChart
import com.filipaeanibal.nutriapp3.models.RecipeInstructions.RecipeInstructions
import com.filipaeanibal.nutriapp3.util.Historico.RecipeHistoryViewModel


@Composable
fun RecipeDetailsPage(
    recipeId: Int,
    onBackClick: () -> Unit,
    viewModel: RecipeDetailsViewModel = hiltViewModel(),
    navController: NavHostController,
    historyViewModel: RecipeHistoryViewModel = hiltViewModel()
) {
    LaunchedEffect(recipeId) {
        viewModel.fetchRecipeDetails(recipeId)
        viewModel.fetchRecipeInstructions(recipeId)
        historyViewModel.checkIfRecipeIsSaved(recipeId)
    }

    val recipeDetailsState by viewModel.recipeDetails.collectAsState()
    val isRecipeSaved by historyViewModel.isRecipeSaved.collectAsState()

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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Text(
                        text = "Recipe Details",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    when (val state = recipeDetailsState) {
                        is NetworkResult.Success -> {
                            state.data?.let { recipeDetails ->
                                IconButton(
                                    onClick = {
                                        historyViewModel.saveRecipe(
                                            recipeId = recipeDetails.id,
                                            title = recipeDetails.title,
                                            image = recipeDetails.image
                                        )
                                        Toast.makeText(
                                            navController.context,
                                            if (isRecipeSaved) "Recipe removed from saved" else "Recipe saved",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                ) {
                                    val tint = if (isRecipeSaved) {
                                        Color.Yellow
                                    } else {
                                        MaterialTheme.colorScheme.onPrimary
                                    }
                                    Icon(
                                        imageVector = Icons.Default.Bookmark,
                                        contentDescription = if (isRecipeSaved) "Remove from Saved" else "Save Recipe",
                                        tint = tint
                                    )
                                }
                            }
                        }
                        else -> {}
                    }
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
                    var selectedServings by remember { mutableStateOf(1.0) }
                    LazyColumn(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            var isVisible by remember { mutableStateOf(false) }
                            LaunchedEffect(Unit) {
                                isVisible = true
                            }

                            val alpha by animateFloatAsState(
                                targetValue = if (isVisible) 1f else 0f,
                                animationSpec = tween(durationMillis = 500)
                            )
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                                    .alpha(alpha),
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
                                            RecipeChip(
                                                icon = {
                                                    Icon(
                                                        Icons.Outlined.Timer,
                                                        contentDescription = "Preparation Time",
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
                                            RecipeChip(
                                                icon = {
                                                    Icon(
                                                        Icons.Outlined.People,
                                                        contentDescription = "Servings",
                                                        tint = Color.White
                                                    )
                                                },
                                                label = {
                                                    Text(
                                                        "${recipeDetails.servings} servings",
                                                        color = Color.White
                                                    )
                                                },
                                            )
                                            RecipeChip(
                                                icon = {
                                                    Icon(
                                                        Icons.Outlined.Favorite,
                                                        contentDescription = "Likes",
                                                        tint = Color.White
                                                    )
                                                },
                                                label = {
                                                    Text(
                                                        "${recipeDetails.aggregateLikes} likes",
                                                        color = Color.White
                                                    )
                                                },
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        item {
                            Text(
                                text = "Nutritional value per serving",
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        item {
                            recipeDetails.nutrition?.nutrients?.let { nutrients ->
                                val mainNutrients = listOf(
                                    NutrientData(
                                        name = "Protein",
                                        amount = nutrients.find { it.name == "Protein" }?.amount ?: 0.0,
                                        unit = "g",
                                        color = NutrientColors.Protein
                                    ),
                                    NutrientData(
                                        name = "Carbs",
                                        amount = nutrients.find { it.name == "Carbohydrates" }?.amount ?: 0.0,
                                        unit = "g",
                                        color = NutrientColors.Carbs
                                    ),
                                    NutrientData(
                                        name = "Fat",
                                        amount = nutrients.find { it.name == "Fat" }?.amount ?: 0.0,
                                        unit = "g",
                                        color = NutrientColors.Fat
                                    )
                                )

                                val calories = nutrients.find { it.name == "Calories" }?.amount?.toInt() ?: 0
                                // defaultAmount = 1.0 pois é 1 serving
                                NutrientPieChart(
                                    nutrients = mainNutrients,
                                    calories = calories,
                                    defaultAmount = 1.0,
                                    onAmountChanged = { selectedServings = it },
                                    isServings = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        item {
                            when (val instructionsState = viewModel.recipeInstructions.collectAsState().value) {
                                is NetworkResult.Success -> {
                                    instructionsState.data?.let { instructions ->
                                        IngredientsSection(
                                            instructions = instructions,
                                            onIngredientClick = { ingredientId, ingredientName ->
                                                navController.navigate("ingredientInformation/$ingredientId/$ingredientName")
                                            }
                                        )
                                    }
                                }
                                is NetworkResult.Loading -> {
                                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                                }
                                is NetworkResult.Error -> {
                                    Text(
                                        text = instructionsState.message ?: "Error loading instructions",
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
                                        text = instructionsState.message ?: "Error loading instructions",
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
                is NetworkResult.Error -> {
                    Text(
                        text = state.message ?: "Unknown error",
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
                text = "Instructions",
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
    onIngredientClick: (Int,String) -> Unit
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
                text = "Ingredients",
                style = MaterialTheme.typography.titleLarge
            )

            val uniqueIngredients = instructions
                .flatMap { it.steps }
                .flatMap { it.ingredients }
                .distinctBy { it.id }

            if (uniqueIngredients.isEmpty()) {
                Text(
                    text = "No ingredients found",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                uniqueIngredients.forEach { ingredient ->
                    var checked by remember { mutableStateOf(false) }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = ingredient.name,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onIngredientClick(ingredient.id, ingredient.name) },
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Checkbox(
                            checked = checked,
                            onCheckedChange = { isChecked ->
                                checked = isChecked
                            }
                        )
                    }
                }
            }
        }
    }
}