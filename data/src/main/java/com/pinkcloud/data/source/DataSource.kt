package com.pinkcloud.data.source

import com.pinkcloud.domain.model.Image
import com.pinkcloud.domain.util.Result

interface DataSource {

    suspend fun getImages(query: String): Result<List<Image>>
}