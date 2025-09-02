package org.michael.kmp.playground

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.michael.kmp.playground.core.network.NetworkResult
import org.michael.kmp.playground.placeholder.domain.models.PostModel
import org.michael.kmp.playground.placeholder.domain.usecases.GetPostByIdUseCase


data class PostsUiState(
    val post: PostModel? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class PostsViewModel(
    private val getPostByIdUseCase: GetPostByIdUseCase // Inyectado por Koin
) : ViewModel() {

    private val _uiState = MutableStateFlow(PostsUiState())
    val uiState: StateFlow<PostsUiState> = _uiState.asStateFlow()

    init {
        // Cargar post 1 automáticamente al iniciar
        loadPost(1)
    }

    fun loadPost(postId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            when (val result = getPostByIdUseCase(postId)) {
                is NetworkResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        post = result.data,
                        isLoading = false
                    )

                    println("Post ${result.data.id} cargado: ${result.data.title}")
                }

                is NetworkResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.exception.message
                    )

                    println("Error cargando post: ${result.exception.message}")
                }
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}