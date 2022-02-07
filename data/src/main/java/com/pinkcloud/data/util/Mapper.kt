package com.pinkcloud.data.util

import com.pinkcloud.data.api.ImageResponse
import com.pinkcloud.data.api.VideoResponse
import com.pinkcloud.data.api.asThumbnail
import com.pinkcloud.domain.model.Thumbnail
import com.pinkcloud.domain.util.Result

fun Result<ImageResponse>.asDomainModel(): Result<List<Thumbnail>> {
    return when (this) {
        is Result.Success -> {
            Result.Success(data!!.imageDocuments.map {
                it.asThumbnail()
            })
        }
        else -> Result.Error(message)
    }
}

@JvmName("asDomainModelVideoResponse")
fun Result<VideoResponse>.asDomainModel(): Result<List<Thumbnail>> {
    return when (this) {
        is Result.Success -> {
            Result.Success(data!!.videoDocuments.map {
                it.asThumbnail()
            })
        }
        else -> Result.Error(message)
    }
}