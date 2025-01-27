package com.filipaeanibal.nutriapp3.models.IngredientInformation

data class Nutrient(
    val amount: Double,
    val name: String,
    val percentOfDailyNeeds: Double,
    val unit: String
)