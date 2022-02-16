package com.pinkcloud.data.api

import com.pinkcloud.domain.model.Thumbnail
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageResponse(
    val meta: Meta,
    val documents: List<ImageDocument>
)

@JsonClass(generateAdapter = true)
data class VideoResponse(
    val meta: Meta,
    val documents: List<VideoDocument>
)

@JsonClass(generateAdapter = true)
data class Meta(
    @Json(name = "total_count")
    val totalCount: Int,
    @Json(name = "pageable_count")
    val pageableCount: Int,
    @Json(name = "is_end")
    val isEnd: Boolean
)

@JsonClass(generateAdapter = true)
data class ImageDocument(
    val collection: String?,
    @Json(name = "thumbnail_url")
    val thumbnailUrl: String?,
    @Json(name = "image_url")
    val imageUrl: String?,
    val width: Int?,
    val height: Int?,
    val datetime: String?
)

@JsonClass(generateAdapter = true)
data class VideoDocument(
    val title: String?,
    @Json(name = "thumbnail")
    val thumbnailUrl: String?,
    @Json(name = "url")
    val videoUrl: String?,
    val datetime: String?,
    val author: String?,
    @Json(name = "play_time")
    val playTime: Int
)

fun ImageDocument.asThumbnail(): Thumbnail {
    return Thumbnail(
        thumbnailUrl = thumbnailUrl,
        datetime = datetime
    )
}

fun VideoDocument.asThumbnail(): Thumbnail {
    return Thumbnail(
        thumbnailUrl = thumbnailUrl,
        datetime = datetime
    )
}