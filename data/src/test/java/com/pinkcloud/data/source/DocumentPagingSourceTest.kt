package com.pinkcloud.data.source

import org.junit.Before
import androidx.paging.PagingSource.LoadResult
import androidx.paging.PagingSource.LoadParams
import com.pinkcloud.data.api.ImageDocument
import com.pinkcloud.data.api.VideoDocument
import com.pinkcloud.data.api.asDocument
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
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
    fun load_nextKeyIs2_whenRefresh() = runTest {
        val expectedList =
            fakeImageDocuments.slice(0 until IMAGE_PAGE_SIZE).map {
                it.asDocument()
            } + fakeVideoDocuments.slice(0 until VIDEO_PAGE_SIZE).map {
                it.asDocument()
            }

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
}