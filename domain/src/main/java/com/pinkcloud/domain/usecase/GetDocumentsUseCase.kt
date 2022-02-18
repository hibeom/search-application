package com.pinkcloud.domain.usecase

import com.pinkcloud.domain.repository.SearchRepository
import javax.inject.Inject

class GetDocumentsUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    operator fun invoke(query: String) = searchRepository.getDocumentPagingFlow(query)
}