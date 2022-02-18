package com.pinkcloud.data.source

import com.pinkcloud.data.api.ImageDocument
import com.pinkcloud.data.api.VideoDocument
import com.pinkcloud.domain.model.Document
import java.util.concurrent.atomic.AtomicInteger

class DocumentFactory {
    private val counter = AtomicInteger(0)
    private fun createDocument(): Document {
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
        collection = null,
        imageUrl = null,
        width = null,
        height = null
    )

    private fun Document.asVideoDocument() = VideoDocument(
        thumbnailUrl = this.thumbnailUrl,
        datetime = this.datetime,
        author = null,
        playTime = null,
        title = null,
        videoUrl = null
    )
}