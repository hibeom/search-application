package com.pinkcloud.searchapplication.ui.storage

import com.pinkcloud.domain.model.Document
import com.pinkcloud.domain.repository.SearchRepository
import com.pinkcloud.domain.usecase.GetSavedDocumentsUseCase
import com.pinkcloud.searchapplication.fake.FakeSearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class StorageViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var storageViewModel: StorageViewModel
    private lateinit var searchRepository: SearchRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        searchRepository = FakeSearchRepository(testDispatcher)
        storageViewModel = StorageViewModel(
            GetSavedDocumentsUseCase(searchRepository)
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun isSavedDocumentsUpdating() = runTest {
        val doc1 = Document("https://thumbnail1", System.currentTimeMillis().toString())
        val doc2 = Document("https://thumbnail2", System.currentTimeMillis().toString())
        val documents = mapOf(
            doc1.thumbnailUrl!! to doc1,
            doc2.thumbnailUrl!! to doc2
        )
        val collectingJob = launch {
            storageViewModel.savedDocuments.collect {}
        }

        async {
            searchRepository.saveDocuments(documents)
        }.await()

        launch {
            storageViewModel.savedDocuments.value.let {
                assertEquals(2, it.size)
            }
        }

        collectingJob.cancel()
    }
}