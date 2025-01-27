package com.filipaeanibal.nutriapp3.models

data class SavedRecipe(
    val id: Int = 0,
    val userId: String = "",
    val title: String = "",
    val image: String = "",
    val savedAt: Long = System.currentTimeMillis()
)
