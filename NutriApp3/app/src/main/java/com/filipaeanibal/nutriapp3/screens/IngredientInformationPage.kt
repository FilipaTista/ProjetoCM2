package com.filipaeanibal.nutriapp3.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.ui.Alignment
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientInformationPage(
    ingredientId: Int,
    onBackClick: () -> Unit,
    viewModel: IngredientInformationViewModel = hiltViewModel()
) {
    // Fetch ingredient information when the page loads
    LaunchedEffect(ingredientId) {
        viewModel.fetchIngredientInformation(ingredientId)
    }

    val ingredientState by viewModel.ingredientInformation.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes do Ingrediente") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = ingredientState) {
                is NetworkResult.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is NetworkResult.Success -> {
                    val ingredient = state.data
                    LazyColumn(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            ingredient.nutrition?.nutrients?.let { nutrients ->
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
                    }
                }
                is NetworkResult.Error -> {
                    Text(
                        text = state.message ?: "Erro ao carregar detalhes do ingrediente.",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}
