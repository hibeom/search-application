package com.pinkcloud.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pinkcloud.data.api.SearchService
import com.pinkcloud.data.api.asDocument
import com.pinkcloud.domain.model.Document
import retrofit2.HttpException
import java.io.IOException

private const val START_PAGE_INDEX = 1
const val PAGE_SIZE = 60
const val VIDEO_PAGE_SIZE = 15
const val IMAGE_PAGE_SIZE = 45
private const val MAX_VIDEO_PAGE = 15
private const val MAX_IMAGE_PAGE = 50

class DocumentPagingSource(
    private val service: SearchService,
    private val query: String
) : PagingSource<Int, Document>() {
    override fun getRefreshKey(state: PagingState<Int, Document>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Document> {
        val position = params.key ?: START_PAGE_INDEX

        return try {
            val (documents, isEnd) = if (position > MAX_VIDEO_PAGE) {
                val imageResponse = service.getImages(query, position, IMAGE_PAGE_SIZE + VIDEO_PAGE_SIZE)
                val documents = imageResponse.documents.map {
                    it.asDocument()
                }
                Pair(documents, imageResponse.meta.isEnd)
            } else {
                val videoResponse = service.getVideos(query, position, VIDEO_PAGE_SIZE)
                val imageResponse = service.getImages(query, position, IMAGE_PAGE_SIZE)
                val imageDocuments = imageResponse.documents.map {
                    it.asDocument()
                }
                val videoDocuments = videoResponse.documents.map {
                    it.asDocument()
                }
                val documents = (imageDocuments + videoDocuments).sortedByDescending { it.datetime }
                Pair(documents, imageResponse.meta.isEnd)
            }

            val nextKey = if (isEnd || documents.isEmpty() || position >= MAX_IMAGE_PAGE) {
                null
            } else {
                position + (params.loadSize / PAGE_SIZE)
            }
            LoadResult.Page(
                data = documents,
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
}