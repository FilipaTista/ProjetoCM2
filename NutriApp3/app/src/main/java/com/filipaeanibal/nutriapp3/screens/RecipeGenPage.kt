package com.filipaeanibal.nutriapp3.screens
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.filipaeanibal.nutriapp3.models.RandomRecipe.RandomRecipe
import com.filipaeanibal.nutriapp3.models.RandomRecipe.Recipe
import com.filipaeanibal.nutriapp3.util.NetworkResult
import com.filipaeanibal.nutriapp3.util.RecipeViewModel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.filipaeanibal.nutriapp3.components.AnimatedSearchBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeGenPage(
    navController: NavController,
    viewModel: RecipeViewModel = hiltViewModel()) {
    val recipeState by viewModel.recipes.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("") }
    val mealTypes = listOf(
        "main course", "side dish", "dessert",
        "appetizer", "salad", "bread",
        "breakfast", "soup", "beverage",
        "breakfast", "soup", "beverage",
        "sauce", "marinade", "snack", "drink"
    )

    // Initial fetch when opening the page
    LaunchedEffect(Unit) {
        viewModel.fetchRandomRecipes(number = 2)
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Descobrir Receitas",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Search and Filter Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AnimatedSearchBar(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        onSearch = {
                            if (searchQuery.isNotBlank()) {
                                viewModel.fetchRandomRecipesByType(
                                    type = searchQuery,
                                    number = 2
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = "Search recipes..."
                    )
                }

                // Meal Type Chips
                LazyRow(
                    modifier = Modifier.padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(mealTypes) { type ->
                        FilterChip(
                            selected = selectedType == type,
                            onClick = {
                                selectedType = if (selectedType == type) "" else type
                                viewModel.fetchRandomRecipesByType(
                                    type = type,
                                    number = 2
                                )
                            },
                            label = { Text(type)},
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                labelColor = MaterialTheme.colorScheme.onSurface,
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary

                            )

                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (val state = recipeState) {
                is NetworkResult.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is NetworkResult.Success -> {
                    val recipes = state.data?.recipes ?: emptyList()
                    LazyColumn(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(recipes) { recipe ->
                            RecipeCard(recipe = recipe,
                                onClick = {
                                    navController.navigate("recipeDetails/${recipe.id}")
                                })
                        }
                    }
                }
                is NetworkResult.Error -> {
                    Log.e("RecipeError", "Error: ${state.message}")
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.message ?: "Erro desconhecido",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RecipeCard(
    recipe: Recipe,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Image
            recipe.image?.let {
                AsyncImage(
                    model = it,
                    contentDescription = recipe.title,
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
                    text = recipe.title,
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
                                "${recipe.readyInMinutes} min",
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
                                "${recipe.servings} porções",
                                color = Color.White
                            )
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun RecipeChip(
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier.height(32.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.White.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            icon()
            label()
        }
    }
}