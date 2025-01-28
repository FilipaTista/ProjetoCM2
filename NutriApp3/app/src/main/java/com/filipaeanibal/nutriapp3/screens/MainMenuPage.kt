package com.filipaeanibal.nutriapp3.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import com.filipaeanibal.nutriapp3.R
import com.filipaeanibal.nutriapp3.util.AuthViewModel

@Composable
fun MainMenuPage(
    onNavigate: (String) -> Unit,
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel()
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Imagem de fundo
            Image(
                painter = painterResource(id = R.drawable.nutriapp_background),
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.7F
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.SpaceBetween,

                ) {
                // Header Section
                Column(
                    modifier = Modifier.padding(top = 150.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Choose an action below to get started.",
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }

                // Main Buttons Section
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Detect Objects Button
                    Button(
                        onClick = { onNavigate("pesquisarAlimentos") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Detect Objects",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Detect Objects", fontSize = 18.sp)
                    }
                    // Generate Recipes Button
                    Button(
                        onClick = { onNavigate("gerarReceitas") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Book,
                            contentDescription = "Generate Recipes",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Generate Recipes", fontSize = 18.sp)
                    }
                    // Recipe History Button (less prominent)
                    OutlinedButton(
                        onClick = { onNavigate("historicoReceitas") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.onBackground
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.History,
                            contentDescription = "Recipe History",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Recipe History", fontSize = 16.sp)
                    }
                }

                // Logout Button Section
                Button(
                    onClick = {
                        authViewModel.logout()
                        navController.navigate("auth") {
                            popUpTo("menu") { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 16.dp, bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    ),
                    elevation = ButtonDefaults.buttonElevation(4.dp)
                ) {
                    Text("Logout", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
