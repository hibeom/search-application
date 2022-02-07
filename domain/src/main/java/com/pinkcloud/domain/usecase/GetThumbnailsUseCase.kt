package com.pinkcloud.domain.usecase

import com.pinkcloud.domain.model.Thumbnail
import com.pinkcloud.domain.repository.SearchRepository
import javax.inject.Inject
import com.pinkcloud.domain.util.Result

class GetThumbnailsUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(query: String): Result<List<Thumbnail>> {
        val imagesResult = searchRepository.getImageThumbnails(query)
        if (imagesResult is Result.Error) return imagesResult
        val videosResult = searchRepository.getVideoThumbnails(query)
        if (videosResult is Result.Error) return videosResult

        val thumbnails = imagesResult.data!! + videosResult.data!!
        return thumbnails.sortedByDescending { it.datetime }.let { sortedThumbnails ->
            Result.Success(sortedThumbnails)
        }
    }
}