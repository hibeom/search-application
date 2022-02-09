package com.pinkcloud.domain.usecase

import com.pinkcloud.domain.model.Thumbnail
import com.pinkcloud.domain.repository.SearchRepository
import javax.inject.Inject

class GetSavedThumbnailsUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(): List<Thumbnail> {
        return searchRepository.getSavedThumbnails().values.toList()
            .sortedByDescending { it.datetime }
    }
}