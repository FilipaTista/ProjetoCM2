package com.filipaeanibal.nutriapp3.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.filipaeanibal.nutriapp3.models.IngredientInformation.IngredientInformation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import retrofit2.Response

@HiltViewModel
class IngredientInformationViewModel @Inject constructor(
    private val ingredientApi: IngredientInformationApi
) : ViewModel() {

    private val _ingredientInformation = MutableStateFlow<NetworkResult<IngredientInformation>>(NetworkResult.Loading())
    val ingredientInformation: StateFlow<NetworkResult<IngredientInformation>> = _ingredientInformation

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
}
