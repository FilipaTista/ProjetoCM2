package com.filipaeanibal.nutriapp3.models.SearchRecipesbyIngredients

data class UsedIngredient(
    val aisle: String,
    val amount: Double,
    val id: Int,
    val image: String,
    val meta: List<Any>,
    val name: String,
    val original: String,
    val originalName: String,
    val unit: String,
    val unitLong: String,
    val unitShort: String
)