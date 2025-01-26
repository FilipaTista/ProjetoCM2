package com.filipaeanibal.nutriapp3.models.RecipeDetails

data class Ingredient(
    val amount: Double,
    val id: Int,
    val name: String,
    val nutrients: List<NutrientX>,
    val unit: String
)