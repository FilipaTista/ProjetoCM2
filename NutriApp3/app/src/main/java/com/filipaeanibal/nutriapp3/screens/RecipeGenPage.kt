package com.filipaeanibal.nutriapp3.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.LocalPizza
import androidx.compose.material.icons.filled.RamenDining
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.outlined.Favorite
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
import com.filipaeanibal.nutriapp3.models.RandomRecipe.Recipe
import com.filipaeanibal.nutriapp3.util.NetworkResult
import com.filipaeanibal.nutriapp3.util.RandomRecipe.RecipeViewModel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
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
    viewModel: RecipeViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val recipeState by viewModel.recipes.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var searchTags by remember { mutableStateOf(listOf<String>()) }
    var selectedType by remember { mutableStateOf("") }
    var showFilterDrawer by remember { mutableStateOf(false) }

    val mealTypes = listOf(
        "main course" to Icons.Default.Restaurant,
        "dessert" to Icons.Default.Cake,
        "breakfast" to Icons.Default.Coffee,
        "soup" to Icons.Default.RamenDining,
        "salad" to Icons.Default.LocalFlorist,
        "snack" to Icons.Default.LocalPizza
    )

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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
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
                        text = "Find Recipes",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    IconButton(onClick = {
                        showFilterDrawer = true
                        selectedType = ""
                    }) {
                        Icon(
                            Icons.Default.FilterList,
                            contentDescription = "More Filters",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
                AnimatedSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = {
                        if (searchQuery.isNotBlank()) {
                            selectedType = ""
                            val newTags = searchQuery.split(" ").filter { it.isNotBlank() }
                            searchTags = (searchTags + newTags).distinct()
                            searchQuery = ""

                            val tagsString = searchTags.joinToString(",")
                            viewModel.fetchRandomRecipesByType(
                                type = tagsString,
                                number = 2
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    placeholder = "Search recipes..."
                )
                if (searchTags.isNotEmpty()) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        items(searchTags) { tag ->
                            FilterChip(
                                selected = true,
                                onClick = {
                                    searchTags = searchTags - tag
                                    if (searchTags.isEmpty()) {
                                        viewModel.fetchRandomRecipes(number = 2)
                                    } else {
                                        val tagsString = searchTags.joinToString(",").lowercase()
                                        viewModel.fetchRandomRecipesByType(
                                            type = tagsString,
                                            number = 2
                                        )
                                    }
                                },
                                label = { Text(tag) },
                                trailingIcon = {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Remove $tag",
                                        modifier = Modifier.size(16.dp)
                                    )
                                },
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
                LazyRow(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(mealTypes) { (type, icon) ->
                        FilterChip(
                            selected = selectedType == type,
                            onClick = {
                                val newType = if (selectedType == type) "" else type.lowercase()
                                selectedType = newType
                                searchTags = emptyList()

                                if (newType.isEmpty()) {
                                    viewModel.fetchRandomRecipes(number = 2)
                                } else {
                                    viewModel.fetchRandomRecipesByType(
                                        type = newType,
                                        number = 2
                                    )
                                }
                            },
                            label = { Text(type) },
                            leadingIcon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
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
                            RecipeCard(
                                recipe = recipe,
                                onClick = {
                                    navController.navigate("recipeDetails/${recipe.id}")
                                }
                            )
                        }
                    }
                }
                is NetworkResult.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.message ?: "An error occurred",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
        if (showFilterDrawer) {
            RecipeFilterDrawer(
                onDismiss = { showFilterDrawer = false },
                onApplyFilters = { filters ->
                    selectedType = ""
                    viewModel.fetchRandomRecipesByType(
                        type = filters,
                        number = 2
                    )
                    searchTags = filters.split(",")
                    showFilterDrawer = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeFilterDrawer(
    onDismiss: () -> Unit,
    onApplyFilters: (String) -> Unit
) {
    var selectedCuisine by remember { mutableStateOf<String?>(null) }
    var selectedDiet by remember { mutableStateOf<String?>(null) }

    val cuisines = listOf(
        "Italian", "Mexican", "Chinese", "Mediterranean", "Indian", "French"
    )
    val diets = listOf(
        "Gluten Free", "Ketogenic", "Vegetarian", "Vegan", "Pescetarian", "Paleo"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxHeight(0.7f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                "Cuisines",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyRow(
                modifier = Modifier.padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cuisines) { cuisine ->
                    FilterChip(
                        selected = selectedCuisine == cuisine,
                        onClick = {
                            selectedCuisine = if (selectedCuisine == cuisine) null else cuisine
                        },
                        label = { Text(cuisine) }
                    )
                }
            }
            Text(
                "Diets",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(diets) { diet ->
                    FilterChip(
                        selected = selectedDiet == diet,
                        onClick = {
                            selectedDiet = if (selectedDiet == diet) null else diet
                        },
                        label = { Text(diet) }
                    )
                }
            }
            Button(
                onClick = {
                    val selectedFilters = listOfNotNull(
                        selectedCuisine?.lowercase(),
                        selectedDiet?.lowercase()
                    ).joinToString(",")

                    if (selectedFilters.isNotEmpty()) {
                        onApplyFilters(selectedFilters)
                    }
                },
                enabled = selectedCuisine != null || selectedDiet != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Apply Filters")
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
                                "${recipe.readyInMinutes} min",
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
                                "${recipe.servings} servings",
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
                                "${recipe.aggregateLikes} likes",
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