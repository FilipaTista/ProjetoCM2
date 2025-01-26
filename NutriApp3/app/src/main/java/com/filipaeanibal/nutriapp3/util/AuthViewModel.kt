package com.filipaeanibal.nutriapp3.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _currentUser = MutableLiveData<FirebaseUser?>()
    val currentUser: LiveData<FirebaseUser?> = _currentUser

    private val _authError = MutableLiveData<String?>()
    val authError: LiveData<String?> = _authError

    // Novo estado para rastrear a primeira execução
    private val _isFirstRun = MutableLiveData<Boolean>(true)
    val isFirstRun: LiveData<Boolean> = _isFirstRun

    init {
        _currentUser.value = auth.currentUser
    }

    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _currentUser.value = auth.currentUser
                    _authError.value = null
                    // Resetar o estado de primeira execução após o login
                    _isFirstRun.value = false
                } else {
                    _authError.value = task.exception?.message ?: "Erro de autenticação"
                }
            }
    }

    fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _currentUser.value = auth.currentUser
                    _authError.value = null
                    // Resetar o estado de primeira execução após o registo
                    _isFirstRun.value = false
                } else {
                    _authError.value = task.exception?.message ?: "Erro de registo"
                }
            }
    }

    fun logout() {
        auth.signOut()
        _currentUser.value = null
        // Resetar o estado de primeira execução após o logout
        _isFirstRun.value = true
    }
    fun onAuthScreenDisplayed() {
        _isFirstRun.value = false
    }
}