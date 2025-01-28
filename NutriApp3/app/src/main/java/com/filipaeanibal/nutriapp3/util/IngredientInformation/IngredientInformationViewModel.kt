package com.filipaeanibal.nutriapp3.util.IngredientInformation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.filipaeanibal.nutriapp3.models.IngredientInformation.IngredientInformation
import com.filipaeanibal.nutriapp3.models.SearchRecipesbyIngredients.SearchRecipesbyIngredientsItem
import com.filipaeanibal.nutriapp3.util.Constants
import com.filipaeanibal.nutriapp3.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import retrofit2.Response

@HiltViewModel
class IngredientInformationViewModel @Inject constructor(
    private val ingredientApi: IngredientInformationApi,
    private val recipesApi: SearchRecipesbyIngredientsApi
) : ViewModel() {

    private val _ingredientInformation = MutableStateFlow<NetworkResult<IngredientInformation>>(
        NetworkResult.Loading()
    )
    val ingredientInformation: StateFlow<NetworkResult<IngredientInformation>> = _ingredientInformation

    private val _recipesByIngredient = MutableStateFlow<NetworkResult<List<SearchRecipesbyIngredientsItem>>>(
        NetworkResult.Loading()
    )
    val recipesByIngredient: StateFlow<NetworkResult<List<SearchRecipesbyIngredientsItem>>> = _recipesByIngredient

    fun fetchIngredientInformation(ingredientId: Int) {
        viewModelScope.launch {
            _ingredientInformation.value = NetworkResult.Loading()
            try {
                val response = ingredientApi.getIngredientInformation(
                    id = ingredientId,
                    apiKey = Constants.API_KEY
                )
                _ingredientInformation.value = handleResponse(response)
            } catch (e: Exception) {
                _ingredientInformation.value = NetworkResult.Error("Erro: ${e.message}")
            }
        }
    }

    private fun handleResponse(response: Response<IngredientInformation>): NetworkResult<IngredientInformation> {
        return if (response.isSuccessful) {
            response.body()?.let {
                NetworkResult.Success(it)
            } ?: NetworkResult.Error("Resposta vazia")
        } else {
            NetworkResult.Error("Erro: ${response.message()}")
        }
    }

    fun fetchRecipesByIngredient(ingredientName: String) {
        val sanitizedIngredient = ingredientName.trim().lowercase() // Remove espaços e padroniza
        viewModelScope.launch {
            _recipesByIngredient.value = NetworkResult.Loading()
            try {
                val response = recipesApi.getRecipesbyIngredients(
                    ingredients = sanitizedIngredient, // Ingrediente fixo para testes
                    apiKey = Constants.API_KEY,
                    number = 3 // Pede 3 receitas
                )
                _recipesByIngredient.value = handleRecipesResponse(response)
                // Logs detalhados para depuração
                Log.d("API_REQUEST", "Ingrediente enviado: $sanitizedIngredient")
                Log.d("API_RESPONSE", "Resposta da API: ${response.body()}")
            } catch (e: Exception) {
                _recipesByIngredient.value = NetworkResult.Error("Erro: ${e.message}")
                Log.e("API_ERROR", "Erro ao buscar receitas: ${e.message}")
            }
        }
    }

    private fun handleRecipesResponse(response: Response<List<SearchRecipesbyIngredientsItem>>): NetworkResult<List<SearchRecipesbyIngredientsItem>> {
        return if (response.isSuccessful) {
            response.body()?.let {
                NetworkResult.Success(it)
            } ?: NetworkResult.Error("Resposta vazia")
        } else {
            NetworkResult.Error("Erro: ${response.message()}")
        }
    }
}