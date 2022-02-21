package com.pinkcloud.data.fake

import com.pinkcloud.data.api.ImageDocument
import com.pinkcloud.data.api.VideoDocument
import com.pinkcloud.domain.model.Document
import java.util.concurrent.atomic.AtomicInteger

class DocumentFactory {
    private val counter = AtomicInteger(0)
    fun createDocument(): Document {
        val id = counter.incrementAndGet()
        return Document(
            thumbnailUrl = "https://thumbnail_url/$id",
            datetime = System.currentTimeMillis().toString()
        )
    }

    fun createImageDocuments(size: Int = 100): List<ImageDocument> {
        val documents = mutableListOf<ImageDocument>()
        for (i in 0 until size) documents.add(createDocument().asImageDocument())
        return documents
    }

    fun createVideoDocuments(size: Int = 100): List<VideoDocument> {
        val documents = mutableListOf<VideoDocument>()
        for (i in 0 until size) documents.add(createDocument().asVideoDocument())
        return documents
    }

    private fun Document.asImageDocument() = ImageDocument(
        thumbnailUrl = this.thumbnailUrl,
        datetime = this.datetime,
        collection = "collection",
        imageUrl = "image_url",
    )

    private fun Document.asVideoDocument() = VideoDocument(
        thumbnailUrl = this.thumbnailUrl,
        datetime = this.datetime,
        author = "author",
        title = "title",
        videoUrl = "video_url"
    )
}