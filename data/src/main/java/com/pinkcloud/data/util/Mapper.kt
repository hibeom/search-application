package com.pinkcloud.data.util

import com.pinkcloud.data.api.ImageResponse
import com.pinkcloud.data.api.asImage
import com.pinkcloud.domain.model.Image
import com.pinkcloud.domain.util.Result

fun Result<ImageResponse>.asDomainModel(): Result<List<Image>> {
    return when (this) {
        is Result.Success -> {
            Result.Success(data!!.documents.map {
                it.asImage()
            })
        }
        else -> Result.Error(message)
    }
}