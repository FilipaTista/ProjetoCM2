package com.filipaeanibal.nutriapp3.util

import com.filipaeanibal.nutriapp3.models.IngredientInformation.IngredientInformation
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IngredientInformationApi {
    @GET("food/ingredients/{id}/information")
    suspend fun getIngredientInformation(
        @Path("id") id: Int,
        @Query("apiKey") apiKey: String,
        @Query("amount") amount: Int = 100,
        @Query("unit") unit: String = "grams",
    ): Response<IngredientInformation>
}