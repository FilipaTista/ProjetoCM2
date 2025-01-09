package com.anibalfilipa.nutriapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.anibalfilipa.nutriapp.ui.theme.NutriAppTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NutriAppTheme {
                // Exibe o Composable
                HomeScreen()
            }
        }
    }
}
@Composable
fun HomeScreen() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Bem-vindo à NutriApp de Nutrição",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = { /* TODO: Navegar para Diário Alimentar */ }) {
            Text(text = "Diário Alimentar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { /* TODO: Navegar para Perfil */ }) {
            Text(text = "Meu Perfil")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Botão para navegar para a tela da câmera
        Button(onClick = {
            val intent = Intent(context, CamaraActivity::class.java)
            context.startActivity(intent)   // Navega para a CamaraActivity
        }) {
            Text(text = "Abrir Câmera")
        }
    }
}