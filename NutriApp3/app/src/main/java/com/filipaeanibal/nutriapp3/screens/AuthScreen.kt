package com.filipaeanibal.nutriapp3.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.filipaeanibal.nutriapp3.R
import com.filipaeanibal.nutriapp3.util.AuthViewModel
import com.google.firebase.auth.FirebaseUser
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(navController: NavHostController, authViewModel: AuthViewModel = viewModel()) {
    val context = LocalContext.current
    val currentUser: FirebaseUser? by authViewModel.currentUser.observeAsState(null)
    val authError: String? by authViewModel.authError.observeAsState(null)
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showEmptyFieldsError by remember { mutableStateOf(false) }

    // Navegar para o menu se o login for bem-sucedido
    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            navController.navigate("menu") {
                popUpTo("auth") { inclusive = true }
            }
        }
    }

    // Exibir mensagens de erro de autenticação
    LaunchedEffect(authError) {
        if (authError != null) {
            Toast.makeText(context, authError, Toast.LENGTH_SHORT).show()
        }
    }

    // Exibir erro caso campos estejam vazios
    LaunchedEffect(showEmptyFieldsError) {
        if (showEmptyFieldsError) {
            Toast.makeText(context, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding() // Adapta ao teclado automaticamente
                .verticalScroll(rememberScrollState()), // Adiciona scroll
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Cabeçalho com imagem
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp) // Altura do cabeçalho
            ) {
                Image(
                    painter = painterResource(id = R.drawable.nutriapp_logo),
                    contentDescription = "App Logo",
                    modifier = Modifier.fillMaxSize(),
                    alignment = Alignment.TopCenter
                )
            }

            // Seção de título
            Text(
                text = "Welcome to NutriApp!",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Campos de email e senha
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Gray
                )
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                singleLine = true,
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = if (showPassword) "Toggle Password Visibility" else "Show Password"
                        )
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Gray
                )
            )

            // Botão de login
            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        showEmptyFieldsError = true
                    } else {
                        authViewModel.login(email, password)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Login", fontSize = 18.sp)
            }

            // Botão de registro
            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        showEmptyFieldsError = true
                    } else {
                        authViewModel.register(email, password)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Register", fontSize = 18.sp)
            }
        }
    }
}