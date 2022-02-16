package com.pinkcloud.searchapplication.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    val thumbnailsState: StateFlow<Result<List<Thumbnail>>>

    private val _query = MutableStateFlow(DEFAULT_QUERY)
    val query: StateFlow<String>
        get() = _query

    init {
        val initialQuery: String = savedStateHandle.get(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        _query.value = initialQuery
        thumbnailsState = query.flatMapLatest {
            flow {
                emit(Result.Loading())
                emit(getThumbnailsUseCase(it))
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), Result.Loading())
    }

    fun search(query: String) {
        _query.value = query
    }

    override fun onCleared() {
        savedStateHandle[LAST_SEARCH_QUERY] = query.value
        super.onCleared()
    }
}

private const val LAST_SEARCH_QUERY: String = "last_search_query"
private const val DEFAULT_QUERY = "kakao"