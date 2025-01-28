package com.filipaeanibal.nutriapp3.util.RandomRecipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.filipaeanibal.nutriapp3.models.RandomRecipe.RandomRecipe
import com.filipaeanibal.nutriapp3.util.Constants
import com.filipaeanibal.nutriapp3.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val apiService: RandomRecipeApi
) : ViewModel() {
    private val _recipes = MutableStateFlow<NetworkResult<RandomRecipe>>(NetworkResult.Loading())
    val recipes: StateFlow<NetworkResult<RandomRecipe>> = _recipes

    fun fetchRandomRecipes(number: Int = 1) {
        viewModelScope.launch {
            _recipes.value = NetworkResult.Loading()
            try {
                val response = apiService.getRandomRecipes(
                    number = number,
                    apiKey = Constants.API_KEY
                )
                _recipes.value = handleResponse(response)
            } catch (e: Exception) {
                _recipes.value = NetworkResult.Error("Erro: ${e.message}")
            }
        }
    }

    fun fetchRandomRecipesByType(type: String, number: Int = 1) {
        viewModelScope.launch {
            _recipes.value = NetworkResult.Loading()
            try {
                val response = apiService.getRandomRecipes(
                    number = number,
                    apiKey = Constants.API_KEY,
                    includeTags = type
                )
                _recipes.value = if (response.isSuccessful) {
                    response.body()?.let {
                        NetworkResult.Success(it)
                    } ?: NetworkResult.Error("Resposta vazia")
                } else {
                    NetworkResult.Error("Erro: ${response.message()}")
                }
            } catch (e: Exception) {
                _recipes.value = NetworkResult.Error("Erro: ${e.message}")
            }
        }
    }


    private fun handleResponse(response: Response<RandomRecipe>): NetworkResult<RandomRecipe> {
        return if (response.isSuccessful) {
            response.body()?.let {
                NetworkResult.Success(it)
            } ?: NetworkResult.Error("Resposta vazia")
        } else {
            NetworkResult.Error("Erro: ${response.message()}")
        }
    }
}