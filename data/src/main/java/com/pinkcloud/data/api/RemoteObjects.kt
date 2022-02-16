package com.pinkcloud.data.api

import com.pinkcloud.domain.model.Image
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageResponse(
    val meta: Meta,
    val documents: List<Document>
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
data class Document(
    val collection: String?,
    @Json(name = "thumbnail_url")
    val thumbnailUrl: String?,
    @Json(name = "image_url")
    val imageUrl: String?,
    val width: Int?,
    val height: Int?,
    val datetime: String?
)

fun Document.asImage(): Image {
    return Image(
        thumbnailUrl = thumbnailUrl,
        datetime = datetime
    )
}