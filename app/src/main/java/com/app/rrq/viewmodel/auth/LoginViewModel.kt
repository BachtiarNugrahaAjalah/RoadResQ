package com.app.rrq.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.app.rrq.core.session.SessionManager
import com.app.rrq.model.data.LoginResult
import com.app.rrq.model.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val result: LoginResult) : LoginState()
    data class Error(val message: String) : LoginState()
}

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginState>(LoginState.Idle)
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = LoginState.Error("Email dan Password tidak boleh kosong")
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginState.Loading
            try {
                val loginResult = loginUseCase(email, password)
                
                // Save session details to SessionManager
                SessionManager.saveSession(
                    userId = loginResult.userId,
                    email = loginResult.email,
                    name = loginResult.name,
                    role = loginResult.role
                )

                _uiState.value = LoginState.Success(loginResult)
            } catch (e: Exception) {
                _uiState.value = LoginState.Error(e.message ?: "Terjadi kesalahan saat masuk")
            }
        }
    }
    
    fun resetState() {
        _uiState.value = LoginState.Idle
    }

    companion object {
        fun provideFactory(
            loginUseCase: LoginUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                    return LoginViewModel(loginUseCase) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
