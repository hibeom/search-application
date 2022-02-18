package com.pinkcloud.domain.usecase

import com.pinkcloud.domain.model.Document
import com.pinkcloud.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSavedDocumentsUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    operator fun invoke(): Flow<List<Document>> {
        return searchRepository.getSavedDocuments().map { map ->
            map.values.toList().sortedByDescending { it.datetime }
        }
    }
}