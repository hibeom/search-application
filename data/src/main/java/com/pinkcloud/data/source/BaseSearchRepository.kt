package com.pinkcloud.data.source

import com.pinkcloud.data.di.DefaultDispatcher
import com.pinkcloud.domain.model.Thumbnail
import com.pinkcloud.domain.repository.SearchRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BaseSearchRepository @Inject constructor(
    private val pagingDataSource: PagingDataSource,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : SearchRepository {

    private val savedThumbnails = mutableMapOf<String, Thumbnail>()

    override fun getThumbnailPagingFlow(query: String) = pagingDataSource.getPagingStream(query)

    override suspend fun saveThumbnails(thumbnails: Map<String, Thumbnail>) =
        withContext(defaultDispatcher) {
            savedThumbnails.putAll(thumbnails)
        }

    override suspend fun getSavedThumbnails() = savedThumbnails
}