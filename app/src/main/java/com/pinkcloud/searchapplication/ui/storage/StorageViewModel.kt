package com.pinkcloud.searchapplication.ui.storage

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinkcloud.domain.model.Document
import com.pinkcloud.domain.usecase.GetSavedDocumentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StorageViewModel @Inject constructor(
    private val getSavedDocumentsUseCase: GetSavedDocumentsUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val savedDocuments = getSavedDocumentsUseCase().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        listOf()
    )
}