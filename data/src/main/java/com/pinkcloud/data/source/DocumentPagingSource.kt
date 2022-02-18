package com.pinkcloud.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pinkcloud.data.api.SearchService
import com.pinkcloud.data.api.asDocument
import com.pinkcloud.domain.model.Document
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
    private val query: String
) : PagingSource<Int, Document>() {

    private var isImagePageEnd = false
    private var isVideoPageEnd = false

    override fun getRefreshKey(state: PagingState<Int, Document>): Int? {
        isImagePageEnd = false
        isVideoPageEnd = false
        // TODO return null 확인
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    // https://stackoverflow.com/questions/64830990/android-paging3-varying-page-size
    // pageSize is just a hint that is sent to LoadParams,
    // paging3 can handle variable page sizes just fine and your PagingSource dorsn't need to respect what loadSize is requested.
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Document> {
        val position = params.key ?: START_PAGE_INDEX

        return try {
            val imageDocuments = if (!isImagePageEnd) {
                val imageResponse =
                    service.getImages(query, position, IMAGE_PAGE_SIZE)
                isImagePageEnd = imageResponse.meta.isEnd
                imageResponse.documents.map {
                    it.asDocument()
                }
            } else listOf()

            val videoDocuments = if (!isVideoPageEnd) {
                val videoResponse =
                    service.getVideos(query, position, VIDEO_PAGE_SIZE)
                isVideoPageEnd = videoResponse.meta.isEnd
                videoResponse.documents.map {
                    it.asDocument()
                }
            } else listOf()

            val documents = (imageDocuments + videoDocuments).sortedByDescending { it.datetime }
            val isEnd = isImagePageEnd && isVideoPageEnd

            val nextKey = if (isEnd || documents.isEmpty() || position >= MAX_IMAGE_PAGE) {
                null
            } else {
                position + 1
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