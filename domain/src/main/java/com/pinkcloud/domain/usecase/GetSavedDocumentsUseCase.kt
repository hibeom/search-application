package com.pinkcloud.domain.usecase

import com.pinkcloud.domain.model.Document
import com.pinkcloud.domain.repository.SearchRepository
import javax.inject.Inject

class GetSavedDocumentsUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(): List<Document> {
        return searchRepository.getSavedDocuments().values.toList()
            .sortedByDescending { it.datetime }
    }
}