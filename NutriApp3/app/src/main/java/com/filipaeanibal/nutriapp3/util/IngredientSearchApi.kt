package com.filipaeanibal.nutriapp3.util

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import com.filipaeanibal.nutriapp3.models.IngredientSearch.IngredientSearch

interface IngredientSearchApi {
    @GET ("food/ingredients/search")
    suspend fun getSearchIngredients(
        @Query("query") query: String,
        @Query("apiKey") apiKey: String,
        @Query("number") number: Int = 2,
    ): Response<IngredientSearch>
}