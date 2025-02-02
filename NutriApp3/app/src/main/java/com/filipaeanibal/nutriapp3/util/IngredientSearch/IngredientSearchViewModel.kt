package com.filipaeanibal.nutriapp3.util.IngredientSearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.filipaeanibal.nutriapp3.models.IngredientSearch.IngredientSearch
import com.filipaeanibal.nutriapp3.util.Constants
import com.filipaeanibal.nutriapp3.util.NetworkResult

@HiltViewModel
class IngredientSearchViewModel @Inject constructor(
    private val apiService: IngredientSearchApi
) : ViewModel() {
    private val _ingredients = MutableStateFlow<NetworkResult<IngredientSearch>>(NetworkResult.Loading())
    val ingredients: StateFlow<NetworkResult<IngredientSearch>> = _ingredients

    fun searchIngredients(query: String) {
        viewModelScope.launch {
            _ingredients.value = NetworkResult.Loading()
            try {
                val response = apiService.getSearchIngredients(
                    query = query,
                    apiKey = Constants.API_KEY
                )
                _ingredients.value = if (response.isSuccessful) {
                    response.body()?.let {
                        NetworkResult.Success(it)
                    } ?: NetworkResult.Error("Empty response")
                } else {
                    NetworkResult.Error("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _ingredients.value = NetworkResult.Error("Error: ${e.message}")
            }
        }
    }
}
