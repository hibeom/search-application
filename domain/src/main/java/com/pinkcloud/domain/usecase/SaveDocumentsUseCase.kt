package com.pinkcloud.domain.usecase

import com.pinkcloud.domain.model.Document
import com.pinkcloud.domain.repository.SearchRepository
import javax.inject.Inject

class SaveDocumentsUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(documents: Map<String, Document>) = searchRepository.saveDocuments(documents)
}