package com.pinkcloud.searchapplication.ui.storage

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinkcloud.domain.model.Document
import com.pinkcloud.domain.usecase.GetSavedDocumentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StorageViewModel @Inject constructor(
    private val getSavedDocumentsUseCase: GetSavedDocumentsUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _savedDocuments = MutableStateFlow(listOf<Document>())
    val savedDocuments: StateFlow<List<Document>>
        get() = _savedDocuments

    init {
        viewModelScope.launch {
            _savedDocuments.value = getSavedDocumentsUseCase()
        }
    }
}