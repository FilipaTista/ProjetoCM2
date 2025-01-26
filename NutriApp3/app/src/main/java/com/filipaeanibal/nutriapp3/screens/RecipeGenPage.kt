package com.filipaeanibal.nutriapp3.screens


import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeGenPage(viewModel: RecipeViewModel = hiltViewModel()) {
    val recipeState by viewModel.recipes.collectAsState()
    var selectedType by remember { mutableStateOf("") }
    val mealTypes = listOf(
        "main course", "side dish", "dessert",
        "appetizer", "salad", "bread",
        "breakfast", "soup", "beverage",
        "sauce", "marinade", "snack", "drink"
    )
    var expanded by remember { mutableStateOf(false) }

    // Busca inicial ao abrir a página
    LaunchedEffect(Unit) {
        viewModel.fetchRandomRecipes(number = 2)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gerador de Receitas") },
                actions = {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it }
                    ) {
                        TextField(
                            value = selectedType.ifEmpty { "Tipo" },
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .menuAnchor()
                                .width(150.dp),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            label = { Text("Filtro de Tipo") }
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            mealTypes.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type) },
                                    onClick = {
                                        selectedType = type
                                        expanded = false
                                        viewModel.fetchRandomRecipesByType(
                                            type = type,
                                            number = 2
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            )
        },
        content = { innerPadding ->
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
                                RecipeItem(recipe)
                            }
                        }
                    }
                    is NetworkResult.Error -> {
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
    )
}

@Composable
fun RecipeItem(recipe: com.filipaeanibal.nutriapp3.models.RandomRecipe.Recipe) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = recipe.title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            recipe.image?.let {
                AsyncImage(
                    model = it,
                    contentDescription = recipe.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Pronto em: ${recipe.readyInMinutes} minutos", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Porções: ${recipe.servings}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Descrição: ${recipe.summary}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

