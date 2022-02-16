package com.pinkcloud.domain.usecase

import com.pinkcloud.domain.model.Thumbnail
import com.pinkcloud.domain.repository.SearchRepository
import javax.inject.Inject
import com.pinkcloud.domain.util.Result

class GetThumbnailsUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    operator fun invoke(query: String) = searchRepository.getThumbnailPagingFlow(query)
}