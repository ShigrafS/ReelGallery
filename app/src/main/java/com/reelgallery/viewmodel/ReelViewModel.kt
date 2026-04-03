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

data class ReelUiState(
    val media: List<MediaItem> = emptyList(),
    val currentIndex: Int = 0,
)

class ReelViewModel(
    private val repository: MediaRepository,
    private val folderId: Long,
    startIndex: Int,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ReelUiState(currentIndex = startIndex))
    val uiState: StateFlow<ReelUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val items = repository.getMediaByFolder(folderId)
            _uiState.value = _uiState.value.copy(media = items)
        }
    }

    fun onVisibleItemChanged(index: Int) {
        if (_uiState.value.currentIndex != index) {
            _uiState.value = _uiState.value.copy(currentIndex = index)
        }
    }

    class Factory(
        private val repository: MediaRepository,
        private val folderId: Long,
        private val startIndex: Int,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ReelViewModel(repository, folderId, startIndex) as T
        }
    }
}
