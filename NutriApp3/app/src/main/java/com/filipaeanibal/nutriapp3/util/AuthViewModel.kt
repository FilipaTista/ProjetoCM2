package com.filipaeanibal.nutriapp3.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {
    private val _currentUser = MutableLiveData<FirebaseUser?>(auth.currentUser)
    val currentUser: LiveData<FirebaseUser?> = _currentUser

    private val _authError = MutableLiveData<String?>()
    val authError: LiveData<String?> = _authError

    private val _isFirstRun = MutableLiveData<Boolean>(true)
    val isFirstRun: LiveData<Boolean> = _isFirstRun

    init {
        _currentUser.value = auth.currentUser
        auth.addAuthStateListener { firebaseAuth ->
            _currentUser.value = firebaseAuth.currentUser
        }
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            _currentUser.value = result.user
            _authError.value = null
        } catch (e: Exception) {
            _authError.value = e.message ?: "Authentication error"
        }
    }

    fun register(email: String, password: String) = viewModelScope.launch {
        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            _currentUser.value = result.user
            _authError.value = null
        } catch (e: Exception) {
            _authError.value = e.message ?: "Error creating account"
        }
    }

    fun logout() {
        auth.signOut()
        _currentUser.value = null
    }

    fun onAuthScreenDisplayed() {
        _isFirstRun.value = false
    }
}