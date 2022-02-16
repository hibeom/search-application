package com.pinkcloud.data.source

import com.pinkcloud.domain.repository.SearchRepository
import javax.inject.Inject

class BaseSearchRepository @Inject constructor(
    private val dataSource: DataSource
) : SearchRepository {
    override suspend fun getImages(query: String) = dataSource.getImages(query)

}