package com.filipaeanibal.nutriapp3.util.RecipeDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.filipaeanibal.nutriapp3.models.RecipeDetails.RecipeDetails
import com.filipaeanibal.nutriapp3.models.RecipeInstructions.RecipeInstructions
import com.filipaeanibal.nutriapp3.util.Constants
import com.filipaeanibal.nutriapp3.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import retrofit2.Response

@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(
    private val apiService: RecipeDetailsApi,
    private val instructionsService: RecipeInstructionsApi
) : ViewModel() {
    private val _recipeDetails = MutableStateFlow<NetworkResult<RecipeDetails>>(NetworkResult.Loading())
    val recipeDetails: StateFlow<NetworkResult<RecipeDetails>> = _recipeDetails

    private val _recipeInstructions = MutableStateFlow<NetworkResult<RecipeInstructions>>(
        NetworkResult.Loading()
    )
    val recipeInstructions: StateFlow<NetworkResult<RecipeInstructions>> = _recipeInstructions

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
                _recipeDetails.value = NetworkResult.Error("Error: ${e.message}")
            }
        }
    }

    private fun handleResponse(response: Response<RecipeDetails>): NetworkResult<RecipeDetails> {
        return if (response.isSuccessful) {
            response.body()?.let {
                NetworkResult.Success(it)
            } ?: NetworkResult.Error("Empty response")
        } else {
            NetworkResult.Error("Error: ${response.message()}")
        }
    }


    fun fetchRecipeInstructions(recipeId: Int) {
        viewModelScope.launch {
            _recipeInstructions.value = NetworkResult.Loading()
            try {
                val response = instructionsService.getRecipeInstructions(
                    id = recipeId,
                    apiKey = Constants.API_KEY
                )
                _recipeInstructions.value = handleInstructionsResponse(response)
            } catch (e: Exception) {
                _recipeInstructions.value = NetworkResult.Error("Error: ${e.message}")
            }
        }
    }


    private fun handleInstructionsResponse(response: Response<RecipeInstructions>): NetworkResult<RecipeInstructions> {
        return if (response.isSuccessful) {
            response.body()?.let {
                NetworkResult.Success(it)
            } ?: NetworkResult.Error("Empty response")
        } else {
            NetworkResult.Error("Error: ${response.message()}")
        }
    }


}