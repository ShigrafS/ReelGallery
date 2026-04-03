package com.reelgallery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.reelgallery.data.MediaRepository
import com.reelgallery.domain.MediaItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class GridUiState(
    val isLoading: Boolean = true,
    val media: List<MediaItem> = emptyList(),
    val scrollToIndex: Int = 0,
)

class GridViewModel(
    private val repository: MediaRepository,
    private val folderId: Long,
) : ViewModel() {
    private val _uiState = MutableStateFlow(GridUiState())
    val uiState: StateFlow<GridUiState> = _uiState.asStateFlow()

    init {
        loadMedia()
    }

    private fun loadMedia() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val items = repository.getMediaByFolder(folderId)
            _uiState.value = _uiState.value.copy(isLoading = false, media = items)
        }
    }

    class Factory(
        private val repository: MediaRepository,
        private val folderId: Long,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GridViewModel(repository, folderId) as T
        }
    }
}
