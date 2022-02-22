package com.pinkcloud.data.source

import com.pinkcloud.data.api.SearchService
import com.pinkcloud.data.fake.DocumentFactory
import com.pinkcloud.data.fake.FAKE_IMAGE_SIZE
import com.pinkcloud.data.fake.FAKE_VIDEO_SIZE
import com.pinkcloud.data.fake.FakeSearchService
import com.pinkcloud.domain.repository.SearchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class BaseSearchRepositoryTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var searchRepository: SearchRepository
    private val fakeSearchService: SearchService
    private val documentFactory = DocumentFactory()

    init {
        val fakeImageDocuments = documentFactory.createImageDocuments(FAKE_IMAGE_SIZE)
        val fakeVideoDocuments = documentFactory.createVideoDocuments(FAKE_VIDEO_SIZE)
        fakeSearchService = FakeSearchService(fakeImageDocuments, fakeVideoDocuments)
    }

    @Before
    fun setup() {
        searchRepository = BaseSearchRepository(
            pagingDataSource = BasePagingDataSource(fakeSearchService, testDispatcher),
            defaultDispatcher = testDispatcher
        )
    }

    @Test
    fun isInitialSavedDocumentsIsEmpty() = runTest {
        val savedDocuments = searchRepository.getSavedDocuments().first()
        assertTrue(savedDocuments.isEmpty())
    }

    @Test
    fun isDocumentsSaved() = testScope.runTest {
        val document1 = documentFactory.createDocument()
        val document2 = documentFactory.createDocument()
        val documentsToBeSaved = mapOf(
            document1.thumbnailUrl to document1,
            document2.thumbnailUrl to document2
        )
        searchRepository.saveDocuments(documentsToBeSaved)
        val savedDocuments = searchRepository.getSavedDocuments().first()
        assertEquals(
            expected = documentsToBeSaved.size,
            actual = savedDocuments.size
        )
    }

    @Test
    fun saveOverlappedDocuments() = testScope.runTest {
        val document1 = documentFactory.createDocument()
        val document2 = documentFactory.createDocument()
        val documentsToBeSaved1 = mapOf(
            document1.thumbnailUrl to document1,
            document2.thumbnailUrl to document2
        )
        val documentsToBeSaved2 = mapOf(
            document1.thumbnailUrl to document1
        )
        searchRepository.saveDocuments(documentsToBeSaved1)
        searchRepository.saveDocuments(documentsToBeSaved2)
        val savedDocuments = searchRepository.getSavedDocuments().first()
        assertEquals(
            expected = documentsToBeSaved1.size,
            actual = savedDocuments.size
        )
    }

}