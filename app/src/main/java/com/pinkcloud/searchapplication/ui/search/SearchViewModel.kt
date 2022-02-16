package com.pinkcloud.searchapplication.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinkcloud.domain.model.Image
import com.pinkcloud.domain.usecase.GetImagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.pinkcloud.domain.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getImagesUseCase: GetImagesUseCase
) : ViewModel() {

    private val _images = MutableStateFlow<List<Image>>(listOf())
    val images: StateFlow<List<Image>>
        get() = _images

    init {
        viewModelScope.launch {
            getImagesUseCase("kakao")
        }
    }
}