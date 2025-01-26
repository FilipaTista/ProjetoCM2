package com.filipaeanibal.nutriapp3.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.filipaeanibal.nutriapp3.util.AuthViewModel
import com.google.firebase.auth.FirebaseUser


@Composable
fun AuthScreen(navController: NavHostController, authViewModel: AuthViewModel = viewModel()) {
    val context = LocalContext.current
    val currentUser: FirebaseUser? by authViewModel.currentUser.observeAsState(null)
    val authError: String? by authViewModel.authError.observeAsState(null)
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showEmptyFieldsError by remember { mutableStateOf(false) }


    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            // Se o utilizador está autenticado, vai para o menu
            navController.navigate("menu") {
                popUpTo("auth") { inclusive = true }
                launchSingleTop = true
            }
        }
    }
    LaunchedEffect(key1 = currentUser) {
        if (currentUser != null && !isFirstRun) {
            Toast.makeText(context, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
            // Navegar para a tela principal (menu)
            navController.navigate("menu") {
                popUpTo("auth") { inclusive = true }
            }
        }
    }

   LaunchedEffect(authError) {
        if (authError != null) {
            Toast.makeText(context, authError, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(showEmptyFieldsError) {
        if (showEmptyFieldsError) {
            Toast.makeText(context, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background

@@ -90,13 +87,16 @@ fun AuthScreen(
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                if (email.isBlank() || password.isBlank()) {
                    showEmptyFieldsError = true
                } else {
                    authViewModel.login(email, password)
                }
            }) {
                Text("Login")
            }
        }
    }
}
