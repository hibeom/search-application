package com.pinkcloud.domain.usecase

import com.pinkcloud.domain.model.Thumbnail
import com.pinkcloud.domain.repository.SearchRepository
import javax.inject.Inject

class SaveThumbnailsUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(thumbnails: Map<String, Thumbnail>) = searchRepository.saveThumbnails(thumbnails)
}