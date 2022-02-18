package com.pinkcloud.data.source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pinkcloud.data.api.SearchService
import com.pinkcloud.domain.model.Document
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BasePagingDataSource @Inject constructor(
    private val searchService: SearchService
) : PagingDataSource {
    override fun getPagingStream(query: String): Flow<PagingData<Document>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE
            ),
            pagingSourceFactory = { DocumentPagingSource(searchService, query) }
        ).flow
    }
}