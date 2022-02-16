package com.pinkcloud.domain.repository

import androidx.paging.PagingData
import com.pinkcloud.domain.model.Thumbnail
import com.pinkcloud.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun getThumbnailPagingFlow(query: String): Flow<PagingData<Thumbnail>>
}