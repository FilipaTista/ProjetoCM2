package com.filipaeanibal.nutriapp3.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.filipaeanibal.nutriapp3.models.IngredientSearch.IngredientSearch

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
                    } ?: NetworkResult.Error("Resposta vazia")
                } else {
                    NetworkResult.Error("Erro: ${response.message()}")
                }
            } catch (e: Exception) {
                _ingredients.value = NetworkResult.Error("Erro: ${e.message}")
            }
        }
    }
}
