package com.pinkcloud.data.source

import com.pinkcloud.domain.repository.SearchRepository
import javax.inject.Inject

class BaseSearchRepository @Inject constructor(
    private val pagingDataSource: PagingDataSource
) : SearchRepository {
    override fun getThumbnailPagingFlow(query: String) = pagingDataSource.getPagingStream(query)
}