package com.pinkcloud.data.source

import com.pinkcloud.domain.repository.SearchRepository
import javax.inject.Inject

class BaseSearchRepository @Inject constructor(
    private val dataSource: DataSource
) : SearchRepository {
    override suspend fun getImageThumbnails(query: String) = dataSource.getImageThumbnails(query)

    override suspend fun getVideoThumbnails(query: String) = dataSource.getVideoThumbnails(query)

}