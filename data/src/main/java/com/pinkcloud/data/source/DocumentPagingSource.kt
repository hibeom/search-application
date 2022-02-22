package com.pinkcloud.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pinkcloud.data.api.DocumentResponse
import com.pinkcloud.data.api.SearchService
import com.pinkcloud.data.api.asDocument
import com.pinkcloud.domain.model.Document
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

private const val START_PAGE_INDEX = 1
const val PAGE_SIZE = 30
const val VIDEO_PAGE_SIZE = 15
const val IMAGE_PAGE_SIZE = 15
private const val MAX_VIDEO_PAGE = 15
private const val MAX_IMAGE_PAGE = 50

class DocumentPagingSource(
    private val service: SearchService,
    private val query: String,
    private val defaultDispatcher: CoroutineDispatcher
) : PagingSource<Int, Document>() {

    private var isImagePageEnd = false
    private var isVideoPageEnd = false
    private val tempImageDocuments = mutableListOf<Document>()
    private val tempVideoDocuments = mutableListOf<Document>()

    override fun getRefreshKey(state: PagingState<Int, Document>): Int? {
        isImagePageEnd = false
        isVideoPageEnd = false
        tempImageDocuments.clear()
        tempVideoDocuments.clear()
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Document> {
        val position = params.key ?: START_PAGE_INDEX
        return try {
            val newImageDocuments = fetchImageDocuments(position)
            val concatenatedImageDocuments = tempImageDocuments + newImageDocuments
            tempImageDocuments.clear()

            val newVideoDocuments = fetchVideoDocuments(position)
            val concatenatedVideoDocuments = tempVideoDocuments + newVideoDocuments
            tempVideoDocuments.clear()

            val lastImageDate = concatenatedImageDocuments.lastOrNull()?.datetime
            val lastVideoDate = concatenatedVideoDocuments.lastOrNull()?.datetime
            val flagDate = getMostRecentDate(lastImageDate, lastVideoDate)

            val imageDocumentsAfterFlagDate =
                getDocumentsAfterDate(concatenatedImageDocuments, flagDate)
            val videoDocumentsAfterFlagDate =
                getDocumentsAfterDate(concatenatedVideoDocuments, flagDate)

            flagDate?.let { date ->
                fillTempDocuments(date, concatenatedImageDocuments, concatenatedVideoDocuments)
            }

            val loadData =
                (imageDocumentsAfterFlagDate + videoDocumentsAfterFlagDate).sortedByDescending { it.datetime }
            val isEnd = isImagePageEnd && isVideoPageEnd

            val nextKey = if (isEnd || loadData.isEmpty() || position >= MAX_IMAGE_PAGE) {
                null
            } else {
                position + 1
            }
            LoadResult.Page(
                data = loadData,
                prevKey = if (position == START_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    private fun DocumentResponse.isEnd() = meta.isEnd || documents.isEmpty()

    private suspend fun fetchImageDocuments(position: Int): List<Document> {
        return if (!isImagePageEnd && position <= MAX_IMAGE_PAGE) {
            val imageResponse = service.getImages(query, position, IMAGE_PAGE_SIZE)
            isImagePageEnd = imageResponse.isEnd()
            imageResponse.documents.map {
                it.asDocument()
            }
        } else listOf()
    }

    private suspend fun fetchVideoDocuments(position: Int): List<Document> {
        return if (!isVideoPageEnd && position <= MAX_VIDEO_PAGE) {
            val videoResponse = service.getVideos(query, position, VIDEO_PAGE_SIZE)
            isVideoPageEnd = videoResponse.isEnd()
            videoResponse.documents.map {
                it.asDocument()
            }
        } else listOf()
    }

    private suspend fun fillTempDocuments(
        flagDate: String,
        imageDocuments: List<Document>,
        videoDocuments: List<Document>
    ) = withContext(defaultDispatcher) {
        imageDocuments.filter { document ->
            document.datetime < flagDate
        }.also { filteredDocuments ->
            tempImageDocuments.addAll(filteredDocuments)
        }
        videoDocuments.filter { document ->
            document.datetime < flagDate
        }.also { filteredDocuments ->
            tempVideoDocuments.addAll(filteredDocuments)
        }
    }

    private fun getMostRecentDate(date1: String?, date2: String?): String? {
        return if (date1 != null && date2 != null) {
            if (date1 > date2) date1 else date2
        } else date1 ?: date2
    }

    private suspend fun getDocumentsAfterDate(
        documents: List<Document>,
        date: String?
    ): List<Document> = withContext(defaultDispatcher) {
        date?.let {
            documents.filter { document ->
                document.datetime >= it
            }
        } ?: run { listOf() }
    }
}