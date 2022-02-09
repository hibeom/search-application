package com.pinkcloud.searchapplication.ui.storage

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinkcloud.domain.model.Thumbnail
import com.pinkcloud.domain.usecase.GetSavedThumbnailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StorageViewModel @Inject constructor(
    private val getSavedThumbnailsUseCase: GetSavedThumbnailsUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _savedThumbnails = MutableStateFlow(listOf<Thumbnail>())
    val savedThumbnails: StateFlow<List<Thumbnail>>
        get() = _savedThumbnails

    init {
        viewModelScope.launch {
            _savedThumbnails.value = getSavedThumbnailsUseCase()
        }
    }
}