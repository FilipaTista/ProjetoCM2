package com.filipaeanibal.nutriapp3.models.RecipeInstructions

data class RecipeInstructionsItem(
    val name: String,
    val steps: List<Step>
)