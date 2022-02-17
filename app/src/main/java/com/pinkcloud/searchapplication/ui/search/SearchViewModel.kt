package com.pinkcloud.searchapplication.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pinkcloud.domain.model.Thumbnail
import com.pinkcloud.domain.usecase.GetThumbnailsUseCase
import com.pinkcloud.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getThumbnailsUseCase: GetThumbnailsUseCase,
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
        _selectedThumbnails.value = mutableMapOf()
    }

    fun save() {
        //TODO save selected thumbnails to repository
    }

    fun onSelectThumbnail(thumbnail: Thumbnail) {
        selectedThumbnails.value[thumbnail.thumbnailUrl]?.let {
            removeSelectedThumbnail(thumbnail)
        } ?: run {
            addSelectedThumbnail(thumbnail)
        }
    }

    private fun addSelectedThumbnail(thumbnail: Thumbnail) {
        _selectedThumbnails.value = selectedThumbnails.value.toMutableMap().also { map ->
            map[thumbnail.thumbnailUrl!!] = thumbnail
        }
    }

    private fun removeSelectedThumbnail(thumbnail: Thumbnail) {
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