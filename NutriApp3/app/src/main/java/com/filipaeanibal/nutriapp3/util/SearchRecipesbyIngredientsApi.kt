package com.filipaeanibal.nutriapp3.util

import com.filipaeanibal.nutriapp3.models.SearchRecipesbyIngredients.SearchRecipesbyIngredientsItem
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Query

interface SearchRecipesbyIngredientsApi {
    @GET("recipes/findByIngredients")
    suspend fun getRecipesbyIngredients(
        @Query("ingredients") ingredients: String,
        @Query("number") number: Int = 3,
        @Query("apiKey") apiKey: String,
        @Query("ranking") ranking: Int = 1,
        @Query("ignorePantry") ignorePantry: Boolean = true,
    ): Response<List<SearchRecipesbyIngredientsItem>>
}
