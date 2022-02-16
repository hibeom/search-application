package com.pinkcloud.domain.usecase

import com.pinkcloud.domain.repository.SearchRepository
import javax.inject.Inject

class GetImagesUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(query: String) = searchRepository.getImages(query)
}