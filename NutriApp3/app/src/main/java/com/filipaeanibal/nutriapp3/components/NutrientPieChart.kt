package com.filipaeanibal.nutriapp3.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType


data class NutrientData(
    val name: String,
    val amount: Double,
    val unit: String,
    val color: Color
)

@Composable
fun NutrientPieChart(
    nutrients: List<NutrientData>,
    calories: Int,
    defaultAmount: Double = 100.0,
    onAmountChanged: (Double) -> Unit,
    isServings: Boolean = false, // Para diferenciar entre g e servings
    modifier: Modifier = Modifier
) {
    var selectedAmount by remember { mutableStateOf(defaultAmount.toString()) }
    val animationProgress = remember { Animatable(0f) }

    LaunchedEffect(nutrients) {
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
        )
    }

    Column(modifier = modifier) {
        // Gráfico e calorias
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            // Gráfico circular
            Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                val radius = minOf(canvasWidth, canvasHeight) / 2.2f
                val centerX = canvasWidth / 2
                val centerY = canvasHeight / 2
                val strokeWidth = radius * 0.25f

                var startAngle = -90f
                val selectedAmountValue = selectedAmount.toDoubleOrNull() ?: defaultAmount
                val total = nutrients.sumOf { (it.amount / defaultAmount) * selectedAmountValue }

                nutrients.forEach { nutrient ->
                    val sweepAngle = (((nutrient.amount / defaultAmount) * selectedAmountValue) / total * 360f * animationProgress.value).toFloat()
                    drawArc(
                        color = nutrient.color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                        size = Size(radius * 2, radius * 2),
                        topLeft = Offset(centerX - radius, centerY - radius)
                    )
                    startAngle += sweepAngle
                }
            }

            // Calorias no centro
            Surface(
                modifier = Modifier.size(100.dp),
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                shadowElevation = 4.dp
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(8.dp)
                ) {
                    val selectedAmountValue = selectedAmount.toDoubleOrNull() ?: defaultAmount
                    val adjustedCalories = (calories * selectedAmountValue / defaultAmount).toInt()
                    Text(
                        text = adjustedCalories.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "calories",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        // Controle de quantidade
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Quantidade:",
                style = MaterialTheme.typography.bodyMedium
            )
            OutlinedTextField(
                value = selectedAmount,
                onValueChange = { newValue ->
                    // Aceita apenas números e ponto
                    if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                        selectedAmount = newValue
                        newValue.toDoubleOrNull()?.let { onAmountChanged(it) }
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(120.dp),
                singleLine = true,
                trailingIcon = {
                    Text(
                        text = if (isServings) "servings" else "g",
                        modifier = Modifier.padding(end = 8.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            )
        }

        // Card nutricional
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Informação Nutricional por ${selectedAmount} ${if (isServings) "servings" else "g"}",
                    style = MaterialTheme.typography.titleMedium
                )

                val selectedAmountValue = selectedAmount.toDoubleOrNull() ?: defaultAmount
                val adjustedCalories = (calories * selectedAmountValue / defaultAmount).toInt()
                Text("Calories: $adjustedCalories kcal")

                nutrients.forEach { nutrient ->
                    val adjustedAmount = (nutrient.amount / defaultAmount) * selectedAmountValue
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            modifier = Modifier.size(12.dp),
                            shape = RoundedCornerShape(2.dp),
                            color = nutrient.color
                        ) {}
                        Text(
                            text = "${nutrient.name}: ${String.format("%.1f", adjustedAmount)}${nutrient.unit}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

// Cores para os nutrientes com ajuste de opacidade para melhor visual
object NutrientColors {
    val Protein = Color(0xFF2196F3).copy(alpha = 0.8f)    // Azul
    val Carbs = Color(0xFF4CAF50).copy(alpha = 0.8f)      // Verde
    val Fat = Color(0xFFFF9800).copy(alpha = 0.8f)        // Laranja
}