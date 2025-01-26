package com.filipaeanibal.nutriapp3.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.filipaeanibal.nutriapp3.util.AuthViewModel
import androidx.navigation.NavHostController 


@Composable
fun MainMenuPage(onNavigate: (String) -> Unit, navController: NavHostController, authViewModel: AuthViewModel = viewModel()) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
             modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
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
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                authViewModel.logout()
                // Navegar de volta para a página de login
                navController.navigate("auth") {
                    popUpTo("menu") { inclusive = true }
                }
            }) {
                Text("Logout")
            }
        }
    }
}
