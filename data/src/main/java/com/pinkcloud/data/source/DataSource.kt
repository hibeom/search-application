package com.pinkcloud.data.source

import com.pinkcloud.domain.model.Thumbnail
import com.pinkcloud.domain.util.Result

interface DataSource {

    suspend fun getImageThumbnails(query: String): Result<List<Thumbnail>>

    suspend fun getVideoThumbnails(query: String): Result<List<Thumbnail>>
}