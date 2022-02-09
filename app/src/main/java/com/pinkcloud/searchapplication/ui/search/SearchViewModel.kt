package com.pinkcloud.searchapplication.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pinkcloud.data.di.DefaultDispatcher
import com.pinkcloud.domain.model.Thumbnail
import com.pinkcloud.domain.usecase.GetThumbnailsUseCase
import com.pinkcloud.domain.usecase.SaveThumbnailsUseCase
import com.pinkcloud.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getThumbnailsUseCase: GetThumbnailsUseCase,
    private val saveThumbnailsUseCase: SaveThumbnailsUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _query = MutableStateFlow(DEFAULT_QUERY)
    val query: StateFlow<String>
        get() = _query

    val pagingDataFlow: Flow<PagingData<Thumbnail>>

    private val _selectedThumbnails = MutableStateFlow(mapOf<String, Thumbnail>())
    val selectedThumbnails: StateFlow<Map<String, Thumbnail>>
        get() = _selectedThumbnails

    init {
        val initialQuery: String = savedStateHandle.get(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        _query.value = initialQuery
        pagingDataFlow = query.flatMapLatest {
            getThumbnailsUseCase(it)
        }.cachedIn(viewModelScope)
    }

    fun search(query: String) {
        _query.value = query
        _selectedThumbnails.value = mapOf()
    }

    fun save() {
        viewModelScope.launch {
            saveThumbnailsUseCase(selectedThumbnails.value)
        }
    }

    fun onSelectThumbnail(thumbnail: Thumbnail) {
        viewModelScope.launch {
            selectedThumbnails.value[thumbnail.thumbnailUrl]?.let {
                removeSelectedThumbnail(thumbnail)
            } ?: run {
                addSelectedThumbnail(thumbnail)
            }
        }
    }

    private suspend fun addSelectedThumbnail(thumbnail: Thumbnail) =
        withContext(defaultDispatcher) {
            _selectedThumbnails.value = selectedThumbnails.value.toMutableMap().also { map ->
                map[thumbnail.thumbnailUrl!!] = thumbnail
            }
        }

    private suspend fun removeSelectedThumbnail(thumbnail: Thumbnail) =
        withContext(defaultDispatcher) {
            _selectedThumbnails.value = selectedThumbnails.value.toMutableMap().also { map ->
                map.remove(thumbnail.thumbnailUrl)
            }
        }

    override fun onCleared() {
        savedStateHandle[LAST_SEARCH_QUERY] = query.value
        super.onCleared()
    }
}

private const val LAST_SEARCH_QUERY: String = "last_search_query"
private const val DEFAULT_QUERY = "kakao"