package com.filipaeanibal.nutriapp3.util


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.filipaeanibal.nutriapp3.models.SavedRecipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class RecipeHistoryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {
    private val _savedRecipes = MutableStateFlow<NetworkResult<List<SavedRecipe>>>(NetworkResult.Loading())
    val savedRecipes: StateFlow<NetworkResult<List<SavedRecipe>>> = _savedRecipes

    private val _isRecipeSaved = MutableStateFlow<Boolean>(false)
    val isRecipeSaved: StateFlow<Boolean> = _isRecipeSaved

    init {
        loadSavedRecipes()
    }

    fun saveRecipe(recipeId: Int, title: String, image: String) {
        viewModelScope.launch {
            try {
                val userId = auth.currentUser?.uid ?: return@launch

                if (_isRecipeSaved.value) {
                    deleteRecipe(recipeId)
                } else {
                    val savedRecipe = SavedRecipe(
                        id = recipeId,
                        userId = userId,
                        title = title,
                        image = image
                    )

                    firestore.collection("recipeHistory")
                        .add(savedRecipe)
                        .await()
                }

                // Toggle the saved state
                _isRecipeSaved.value = !_isRecipeSaved.value
                loadSavedRecipes()
            } catch (e: Exception) {
                _savedRecipes.value = NetworkResult.Error("Erro ao salvar receita: ${e.message}")
            }
        }
    }

    fun checkIfRecipeIsSaved(recipeId: Int) {
        viewModelScope.launch {
            try {
                val userId = auth.currentUser?.uid ?: return@launch

                val query = firestore.collection("recipeHistory")
                    .whereEqualTo("userId", userId)
                    .whereEqualTo("id", recipeId)
                    .get()
                    .await()

                _isRecipeSaved.value = !query.documents.isEmpty()
            } catch (e: Exception) {
                _isRecipeSaved.value = false
            }
        }
    }

    fun loadSavedRecipes() {
        viewModelScope.launch {
            try {
                _savedRecipes.value = NetworkResult.Loading()

                val userId = auth.currentUser?.uid ?: return@launch

                val recipes = firestore.collection("recipeHistory")
                    .whereEqualTo("userId", userId)
                    .orderBy("savedAt", Query.Direction.DESCENDING)
                    .get()
                    .await()
                    .toObjects(SavedRecipe::class.java)

                _savedRecipes.value = NetworkResult.Success(recipes)
            } catch (e: Exception) {
                _savedRecipes.value = NetworkResult.Error("Erro ao carregar receitas: ${e.message}")
            }
        }
    }

    fun deleteRecipe(recipeId: Int) {
        viewModelScope.launch {
            try {
                val userId = auth.currentUser?.uid ?: return@launch  // Usando Firebase Auth diretamente

                val query = firestore.collection("recipeHistory")
                    .whereEqualTo("userId", userId)
                    .whereEqualTo("id", recipeId)
                    .get()
                    .await()

                for (document in query.documents) {
                    document.reference.delete().await()
                }

                loadSavedRecipes()
            } catch (e: Exception) {
                _savedRecipes.value = NetworkResult.Error("Erro ao deletar receita: ${e.message}")
            }
        }
    }
}