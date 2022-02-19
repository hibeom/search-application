package com.pinkcloud.searchapplication.fake

import androidx.paging.PagingData
import com.pinkcloud.data.di.DefaultDispatcher
import com.pinkcloud.domain.model.Document
import com.pinkcloud.domain.repository.SearchRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class FakeSearchRepository(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    ) : SearchRepository {

    private val savedDocuments = MutableStateFlow(mapOf<String, Document>())

    override fun getDocumentPagingFlow(query: String): Flow<PagingData<Document>> {
        return flow {  }
    }

    override suspend fun saveDocuments(documents: Map<String, Document>) =
        withContext(defaultDispatcher) {
            savedDocuments.value = savedDocuments.value.toMutableMap().also { map ->
                map.putAll(documents)
            }
        }

    override fun getSavedDocuments() = savedDocuments
}