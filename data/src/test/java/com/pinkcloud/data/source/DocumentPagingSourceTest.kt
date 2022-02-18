package com.pinkcloud.data.source

import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import com.pinkcloud.data.api.ImageDocument
import com.pinkcloud.data.api.VideoDocument
import com.pinkcloud.data.api.asDocument
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class DocumentPagingSourceTest {

    private val fakeSearchService: FakeSearchService

    private lateinit var pagingSource: DocumentPagingSource
    private val fakeQuery = "fake query"

    private val fakeImageDocuments: List<ImageDocument>
    private val fakeVideoDocuments: List<VideoDocument>

    init {
        val documentFactory = DocumentFactory()
        fakeImageDocuments = documentFactory.createImageDocuments(FAKE_IMAGE_SIZE)
        fakeVideoDocuments = documentFactory.createVideoDocuments(FAKE_VIDEO_SIZE)
        fakeSearchService = FakeSearchService(fakeImageDocuments, fakeVideoDocuments)
    }

    @Before
    fun initializePagingSource() {
        pagingSource = DocumentPagingSource(fakeSearchService, fakeQuery)
    }

    @Test
    fun load_refreshResult() = runTest {
        val expectedImages = fakeImageDocuments
            .slice(0 until IMAGE_PAGE_SIZE)
            .map {
                it.asDocument()
            }
        val expectedVideos = fakeVideoDocuments
            .slice(0 until VIDEO_PAGE_SIZE)
            .map {
                it.asDocument()
            }
        val expectedList = (expectedImages + expectedVideos).sortedByDescending { it.datetime }

        assertEquals(
            expected = LoadResult.Page(
                data = expectedList,
                prevKey = null,
                nextKey = 2
            ),
            actual = pagingSource.load(
                LoadParams.Refresh(
                    key = null,
                    loadSize = IMAGE_PAGE_SIZE + VIDEO_PAGE_SIZE,
                    placeholdersEnabled = true
                )
            )
        )
    }

    @Test
    fun load_overVideoSize() = runTest {
        val page = FAKE_VIDEO_SIZE / VIDEO_PAGE_SIZE + 1
        val expectedImages = fakeImageDocuments
            .slice((page - 1) * IMAGE_PAGE_SIZE until (page) * IMAGE_PAGE_SIZE)
            .map {
                it.asDocument()
            }
        val expectedVideos = fakeVideoDocuments
            .slice((page - 1) * VIDEO_PAGE_SIZE until FAKE_VIDEO_SIZE)
            .map {
                it.asDocument()
            }
        val expectedList = (expectedImages + expectedVideos).sortedByDescending { it.datetime }

        assertEquals(
            expected = LoadResult.Page(
                data = expectedList,
                prevKey = page - 1,
                nextKey = page + 1
            ),
            actual = pagingSource.load(
                LoadParams.Append(
                    key = page,
                    loadSize = IMAGE_PAGE_SIZE + VIDEO_PAGE_SIZE,
                    placeholdersEnabled = true
                )
            )
        )
    }

    @Test
    fun load_isEnd() = runTest {
        val page = FAKE_IMAGE_SIZE / IMAGE_PAGE_SIZE + 1

        val expectedList = fakeImageDocuments
            .slice((page - 1) * IMAGE_PAGE_SIZE until FAKE_IMAGE_SIZE)
            .map {
                it.asDocument()
            }.sortedByDescending { it.datetime }

        assertEquals(
            expected = LoadResult.Page(
                data = expectedList,
                prevKey = page - 1,
                nextKey = null
            ),
            actual = pagingSource.load(
                LoadParams.Append(
                    key = page,
                    loadSize = IMAGE_PAGE_SIZE + VIDEO_PAGE_SIZE,
                    placeholdersEnabled = true
                )
            )
        )
    }
}