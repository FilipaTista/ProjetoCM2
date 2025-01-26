package com.filipaeanibal.nutriapp3.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.filipaeanibal.nutriapp3.models.RecipeDetails.RecipeDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import retrofit2.Response

@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(
    private val apiService: RecipeDetailsApi
) : ViewModel() {
    private val _recipeDetails = MutableStateFlow<NetworkResult<RecipeDetails>>(NetworkResult.Loading())
    val recipeDetails: StateFlow<NetworkResult<RecipeDetails>> = _recipeDetails

    fun fetchRecipeDetails(recipeId: Int) {
        viewModelScope.launch {
            _recipeDetails.value = NetworkResult.Loading()
            try {
                val response = apiService.getRecipeDetails(
                    id = recipeId,
                    apiKey = Constants.API_KEY,
                    includeNutrition=true
                )
                _recipeDetails.value = handleResponse(response)
            } catch (e: Exception) {
                _recipeDetails.value = NetworkResult.Error("Erro: ${e.message}")
            }
        }
    }

    private fun handleResponse(response: Response<RecipeDetails>): NetworkResult<RecipeDetails> {
        return if (response.isSuccessful) {
            response.body()?.let {
                NetworkResult.Success(it)
            } ?: NetworkResult.Error("Resposta vazia")
        } else {
            NetworkResult.Error("Erro: ${response.message()}")
        }
    }
}