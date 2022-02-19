package com.pinkcloud.data.fake

import com.pinkcloud.data.api.*

const val FAKE_IMAGE_SIZE = 200
const val FAKE_VIDEO_SIZE = 80

class FakeSearchService(
    private val imageDocuments: List<ImageDocument>,
    private val videoDocuments: List<VideoDocument>
) : SearchService {

    override suspend fun getImages(
        query: String,
        page: Int,
        size: Int,
        sort: String
    ): ImageResponse {
        val meta = Meta(
            FAKE_IMAGE_SIZE, FAKE_IMAGE_SIZE,
            isEnd = (page + 1) * size > FAKE_IMAGE_SIZE
        )
        val fromIndex = (page - 1) * size
        val toIndex = if (page * size > FAKE_IMAGE_SIZE) FAKE_IMAGE_SIZE else page * size
        return ImageResponse(
            meta = meta,
            documents = if (fromIndex < FAKE_IMAGE_SIZE) imageDocuments.subList(
                fromIndex,
                toIndex
            ) else listOf()
        )
    }

    override suspend fun getVideos(
        query: String,
        page: Int,
        size: Int,
        sort: String
    ): VideoResponse {
        val meta = Meta(
            FAKE_VIDEO_SIZE, FAKE_VIDEO_SIZE,
            isEnd = (page + 1) * size > FAKE_VIDEO_SIZE
        )
        val fromIndex = (page - 1) * size
        val toIndex = if (page * size > FAKE_VIDEO_SIZE) FAKE_VIDEO_SIZE else page * size
        return VideoResponse(
            meta = meta,
            documents = if (fromIndex < FAKE_VIDEO_SIZE) videoDocuments.subList(
                fromIndex,
                toIndex
            ) else listOf()
        )
    }
}