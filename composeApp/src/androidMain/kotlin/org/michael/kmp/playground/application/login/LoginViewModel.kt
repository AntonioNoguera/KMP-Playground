package org.michael.kmp.playground.application.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.michael.kmp.playground.core.network.NetworkResult
import org.michael.kmp.playground.auth.domain.models.AuthModel
import org.michael.kmp.playground.auth.domain.usecases.LoginParams
import org.michael.kmp.playground.auth.domain.usecases.LoginUseCase

data class PostsUiState(
    val data: AuthModel? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class PostsViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PostsUiState())
    val uiState: StateFlow<PostsUiState> = _uiState.asStateFlow()

    init {
        //Por acá llamadas a servicios por defecto!
    }

    fun login( email: String, password: String) {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            when (val result = loginUseCase(LoginParams(email, password))) {
                is NetworkResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        data = result.data,
                        isLoading = false
                    )

                    println("Login exitoso: ${result.data.token}")
                }

                is NetworkResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.exception.message
                    )
                    println("Error en login: ${result.exception.message}")
                }
            }


        }
    }
    fun clearUser() {
        _uiState.value = _uiState.value.copy(
            data = null,
            errorMessage = null
        )
    }
}