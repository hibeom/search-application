package com.pinkcloud.searchapplication.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pinkcloud.data.di.DefaultDispatcher
import com.pinkcloud.domain.model.Document
import com.pinkcloud.domain.usecase.GetDocumentsUseCase
import com.pinkcloud.domain.usecase.SaveDocumentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getDocumentsUseCase: GetDocumentsUseCase,
    private val saveDocumentsUseCase: SaveDocumentsUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _query = MutableStateFlow(DEFAULT_QUERY)
    val query: StateFlow<String>
        get() = _query

    val pagingDataFlow: Flow<PagingData<Document>>

    private val _selectedDocuments = MutableStateFlow(mapOf<String, Document>())
    val selectedDocuments: StateFlow<Map<String, Document>>
        get() = _selectedDocuments

    private val _isSaveCompleted = MutableStateFlow(false)
    val isSaveCompleted: StateFlow<Boolean>
        get() = _isSaveCompleted

    init {
        val initialQuery: String = savedStateHandle.get(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        _query.value = initialQuery
        pagingDataFlow = query.flatMapLatest {
            getDocumentsUseCase(it)
        }.cachedIn(viewModelScope)
    }

    fun search(query: String) {
        _query.value = query
        _selectedDocuments.value = mapOf()
    }

    fun save() {
        viewModelScope.launch(defaultDispatcher) {
            saveDocumentsUseCase(selectedDocuments.value)
            resetSelectedDocuments()
            _isSaveCompleted.value = true
        }
    }

    fun resetSaveCompleted() {
        _isSaveCompleted.value = false
    }

    private fun resetSelectedDocuments() {
        selectedDocuments.value.values.forEach { it.isSelected = false }
        _selectedDocuments.value = mapOf()
    }

    fun onSelectDocument(document: Document) {
        viewModelScope.launch(defaultDispatcher) {
            selectedDocuments.value[document.thumbnailUrl]?.let {
                removeSelectedDocument(document)
            } ?: run {
                addSelectedDocument(document)
            }
        }
    }

    private fun addSelectedDocument(document: Document) {
        _selectedDocuments.value = selectedDocuments.value.toMutableMap().also { map ->
            map[document.thumbnailUrl!!] = document
        }
    }

    private fun removeSelectedDocument(document: Document) {
        _selectedDocuments.value = selectedDocuments.value.toMutableMap().also { map ->
            map.remove(document.thumbnailUrl)
        }
    }

    override fun onCleared() {
        savedStateHandle[LAST_SEARCH_QUERY] = query.value
        super.onCleared()
    }
}

private const val LAST_SEARCH_QUERY: String = "last_search_query"
private const val DEFAULT_QUERY = "kakao"