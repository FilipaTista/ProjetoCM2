package com.filipaeanibal.nutriapp3.util

import com.filipaeanibal.nutriapp3.models.RecipeDetails.RecipeDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeDetailsApi {
    @GET("recipes/{id}/information")
    suspend fun getRecipeDetails(
        @Path("id") id: Int,
        @Query("apiKey") apiKey: String,
        @Query("includeNutrition") includeNutrition: Boolean = true,
    ): Response<RecipeDetails>
}