package com.pinkcloud.domain.repository

import com.pinkcloud.domain.model.Image
import com.pinkcloud.domain.util.Result

interface SearchRepository {

    suspend fun getImages(query: String): Result<List<Image>>
}