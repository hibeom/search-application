package com.pinkcloud.data.api

import com.pinkcloud.domain.model.Document
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageResponse(
    override val meta: Meta,
    override val documents: List<ImageDocument>
) : DocumentResponse

@JsonClass(generateAdapter = true)
data class VideoResponse(
    override val meta: Meta,
    override val documents: List<VideoDocument>
) : DocumentResponse

interface DocumentResponse {
    val meta: Meta
    val documents: List<NetworkDocument>
}

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
    val collection: String,
    @Json(name = "thumbnail_url")
    override val thumbnailUrl: String,
    @Json(name = "image_url")
    val imageUrl: String,
    override val datetime: String
) : NetworkDocument

@JsonClass(generateAdapter = true)
data class VideoDocument(
    val title: String,
    @Json(name = "thumbnail")
    override val thumbnailUrl: String,
    @Json(name = "url")
    val videoUrl: String,
    override val datetime: String,
    val author: String,
) : NetworkDocument

interface NetworkDocument {
    val thumbnailUrl: String
    val datetime: String
}

fun NetworkDocument.asDocument() = Document(thumbnailUrl, datetime)