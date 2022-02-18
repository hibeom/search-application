package com.pinkcloud.data.source

import com.pinkcloud.data.di.DefaultDispatcher
import com.pinkcloud.domain.model.Document
import com.pinkcloud.domain.repository.SearchRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BaseSearchRepository @Inject constructor(
    private val pagingDataSource: PagingDataSource,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : SearchRepository {

    private val savedDocuments = mutableMapOf<String, Document>()

    override fun getDocumentPagingFlow(query: String) = pagingDataSource.getPagingStream(query)

    override suspend fun saveDocuments(documents: Map<String, Document>) =
        withContext(defaultDispatcher) {
            savedDocuments.putAll(documents)
        }

    override suspend fun getSavedDocuments() = savedDocuments
}