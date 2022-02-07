package com.pinkcloud.domain.repository

import com.pinkcloud.domain.model.Thumbnail
import com.pinkcloud.domain.util.Result

interface SearchRepository {

    suspend fun getImageThumbnails(query: String): Result<List<Thumbnail>>

    suspend fun getVideoThumbnails(query: String): Result<List<Thumbnail>>
}