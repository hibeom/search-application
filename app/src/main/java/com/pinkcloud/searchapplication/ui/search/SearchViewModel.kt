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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        val targetDocuments = selectedDocuments.value
        viewModelScope.launch {
            saveDocumentsUseCase(targetDocuments)
        }
        selectedDocuments.value.values.forEach { it.isSelected = false }
        _selectedDocuments.value = mapOf()
    }

    fun onSelectDocument(document: Document) {
        viewModelScope.launch {
            selectedDocuments.value[document.thumbnailUrl]?.let {
                removeSelectedDocument(document)
            } ?: run {
                addSelectedDocument(document)
            }
        }
    }

    private suspend fun addSelectedDocument(document: Document) =
        withContext(defaultDispatcher) {
            _selectedDocuments.value = selectedDocuments.value.toMutableMap().also { map ->
                map[document.thumbnailUrl!!] = document
            }
        }

    private suspend fun removeSelectedDocument(document: Document) =
        withContext(defaultDispatcher) {
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