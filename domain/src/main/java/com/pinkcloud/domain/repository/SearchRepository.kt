package com.pinkcloud.domain.repository

import androidx.paging.PagingData
import com.pinkcloud.domain.model.Document
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun getDocumentPagingFlow(query: String): Flow<PagingData<Document>>

    suspend fun saveDocuments(documents: Map<String, Document>)

    suspend fun getSavedDocuments(): Map<String, Document>
}