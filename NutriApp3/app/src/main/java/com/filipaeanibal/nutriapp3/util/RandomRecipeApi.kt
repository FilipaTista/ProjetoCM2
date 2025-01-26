package com.filipaeanibal.nutriapp3.util

import retrofit2.Response
import com.filipaeanibal.nutriapp3.models.RandomRecipe.RandomRecipe
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomRecipeApi {
    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("number") number: Int = 1,
        @Query("apiKey") apiKey: String,
        @Query("includeNutrition") includeNutrition: Boolean = false,
        @Query("include-tags") includeTags: String? = null,
    ): Response<RandomRecipe>
}