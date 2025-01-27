package com.filipaeanibal.nutriapp3.models.FoodDetector

import com.filipaeanibal.nutriapp3.screens.BoundingBox


fun getLabelFromOutput(output: FloatArray): String {
    val labels = listOf("Apple", "Banana", "Orange")
    val maxIndex = output.indices.maxByOrNull { output[it] } ?: 0
    return labels[maxIndex]
}

fun calculateBoundingBox(output: FloatArray): BoundingBox? {
    return if (output.size >= 4) {
        BoundingBox(
            left = output[0],
            top = output[1],
            right = output[2],
            bottom = output[3]
        )
    } else {
        null
    }
}

