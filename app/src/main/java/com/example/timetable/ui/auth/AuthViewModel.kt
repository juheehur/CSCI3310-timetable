package com.example.timetable.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
    data class Authenticated(val user: FirebaseUser) : AuthState()
    object Unauthenticated : AuthState()
}

class AuthViewModel : ViewModel() {
    
    private val auth = FirebaseAuth.getInstance()
    
    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState
    
    init {
        // 현재 인증 상태 확인
        val currentUser = auth.currentUser
        if (currentUser != null) {
            _authState.value = AuthState.Authenticated(currentUser)
        } else {
            _authState.value = AuthState.Unauthenticated
        }
    }
    
    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading
                val result = auth.signInWithEmailAndPassword(email, password).await()
                result.user?.let {
                    _authState.value = AuthState.Authenticated(it)
                } ?: run {
                    _authState.value = AuthState.Error("로그인 실패: 알 수 없는 오류")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("로그인 실패: ${e.message}")
            }
        }
    }
    
    fun register(email: String, password: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                result.user?.let {
                    _authState.value = AuthState.Authenticated(it)
                } ?: run {
                    _authState.value = AuthState.Error("회원가입 실패: 알 수 없는 오류")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("회원가입 실패: ${e.message}")
            }
        }
    }
    
    fun logout() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }
} 