package com.pinkcloud.data.source

import androidx.paging.PagingData
import com.pinkcloud.domain.model.Thumbnail
import kotlinx.coroutines.flow.Flow

interface PagingDataSource {

    fun getPagingStream(query: String): Flow<PagingData<Thumbnail>>
}