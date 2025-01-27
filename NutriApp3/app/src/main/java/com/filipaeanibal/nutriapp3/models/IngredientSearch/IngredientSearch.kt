package com.filipaeanibal.nutriapp3.models.IngredientSearch

data class IngredientSearch(
    val number: Int,
    val offset: Int,
    val results: List<Result>,
    val totalResults: Int
)