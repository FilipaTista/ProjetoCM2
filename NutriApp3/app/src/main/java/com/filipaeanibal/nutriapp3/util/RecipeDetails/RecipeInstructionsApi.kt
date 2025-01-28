package com.filipaeanibal.nutriapp3.util.RecipeDetails


import com.filipaeanibal.nutriapp3.models.RecipeInstructions.RecipeInstructions
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeInstructionsApi {
    @GET("recipes/{id}/analyzedInstructions")
    suspend fun getRecipeInstructions(
        @Path("id") id: Int,
        @Query("apiKey") apiKey: String,
    ): Response<RecipeInstructions>
}