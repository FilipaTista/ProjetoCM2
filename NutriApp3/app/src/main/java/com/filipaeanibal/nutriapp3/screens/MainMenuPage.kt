package com.filipaeanibal.nutriapp3.screens

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp


@Composable
fun MainMenuPage(onNavigate: (String) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { onNavigate("detectarObjetos") }) {
                Text("Detetar Objetos")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { onNavigate("gerarReceitas") }) {
                Text("Gerar Receitas")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { onNavigate("historicoReceitas") }) {
                Text("Histórico de Receitas")
            }
        }
    }
}
