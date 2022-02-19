package com.pinkcloud.searchapplication.ui.search

import androidx.lifecycle.SavedStateHandle
import com.pinkcloud.domain.model.Document
import com.pinkcloud.domain.repository.SearchRepository
import com.pinkcloud.domain.usecase.GetDocumentsUseCase
import com.pinkcloud.domain.usecase.SaveDocumentsUseCase
import com.pinkcloud.searchapplication.fake.FakeSearchRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var searchRepository: SearchRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        searchRepository = FakeSearchRepository(testDispatcher)
        searchViewModel = SearchViewModel(
            GetDocumentsUseCase(searchRepository),
            SaveDocumentsUseCase(searchRepository),
            testDispatcher,
            SavedStateHandle()
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun search_isQuerySet() {
        val query = "fake query"
        searchViewModel.search(query)

        assertEquals(query ,searchViewModel.query.value)
    }

    /**
     * async -> await end -> addSelectedDocument -> launch
     */
    @Test
    fun onSelectDocument_selectTwoDocuments() = runTest {
        val doc1 = Document("https://thumbnail1", System.currentTimeMillis().toString())
        val doc2 = Document("https://thumbnail2", System.currentTimeMillis().toString())
        val differed = async {
            searchViewModel.onSelectDocument(doc1)
            searchViewModel.onSelectDocument(doc2)
        }
        differed.await()

        launch {
            searchViewModel.selectedDocuments.value.let {
                assertEquals(2, it.size)
            }
        }
    }

    @Test
    fun onSelectDocument_selectTwoDocuments_thenSelectOneAgain() = runTest {
        val doc1 = Document("https://thumbnail1", System.currentTimeMillis().toString())
        val doc2 = Document("https://thumbnail2", System.currentTimeMillis().toString())
        val differed = async {
            searchViewModel.onSelectDocument(doc1)
            searchViewModel.onSelectDocument(doc2)
        }
        differed.await()
        val differed2 = async {
            searchViewModel.onSelectDocument(doc1)
        }
        differed2.await()

        launch {
            searchViewModel.selectedDocuments.value.let {
                assertEquals(1, it.size)
            }
        }
    }

    @Test
    fun save() = runTest {
        val doc1 = Document("https://thumbnail1", System.currentTimeMillis().toString())
        val doc2 = Document("https://thumbnail2", System.currentTimeMillis().toString())

        val differed = async {
            searchViewModel.onSelectDocument(doc1)
            searchViewModel.onSelectDocument(doc2)
        }
        differed.await()

        val differed2 = async {
            searchViewModel.save()
        }
        differed2.await()

        launch {
            searchViewModel.selectedDocuments.value.let {
                assertEquals(0, it.size)
            }
            searchRepository.getSavedDocuments().first().let {
                assertEquals(2, it.size)
            }
        }
    }
}