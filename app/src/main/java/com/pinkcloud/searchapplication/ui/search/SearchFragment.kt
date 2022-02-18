package com.pinkcloud.searchapplication.ui.search

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.pinkcloud.domain.model.Document
import com.pinkcloud.searchapplication.databinding.SearchFragmentBinding
import com.pinkcloud.searchapplication.util.calculateSpanCount
import com.pinkcloud.searchapplication.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: SearchFragmentBinding
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SearchFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.setSearchInputListener(
            onSearch = { query ->
                viewModel.search(query)
            }
        )
        binding.setSearchResult(
            pagingDataFlow = viewModel.pagingDataFlow,
            onClickDocument = { document ->
                viewModel.onSelectDocument(document)
            }
        )

        return binding.root
    }

    private fun SearchFragmentBinding.setSearchInputListener(
        onSearch: (String) -> Unit
    ) {
        searchTextInput.apply {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    hideKeyboard(context, this)
                    searchInput(onSearch)
                    true
                } else false
            }
            setOnKeyListener { _, keyCode, keyEvent ->
                if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchInput(onSearch)
                    true
                } else false
            }
            setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus) hideKeyboard(context, view)
            }
        }
    }

    private fun SearchFragmentBinding.searchInput(onQueryChanged: (String) -> Unit) {
        searchTextInput.text!!.trim().let {
            if (it.isNotEmpty()) {
                list.scrollToPosition(0)
                onQueryChanged(it.toString())
            }
        }
    }

    private fun SearchFragmentBinding.setSearchResult(
        pagingDataFlow: Flow<PagingData<Document>>,
        onClickDocument: (Document) -> Unit
    ) {
        val spanCount = calculateSpanCount(requireActivity())
        val footerAdapter = DocumentLoadStateAdapter()
        val documentAdapter = DocumentPagingAdapter(onClickDocument)
        list.apply {
            adapter = documentAdapter.withLoadStateFooter(
                footer = footerAdapter
            )
            layoutManager = GridLayoutManager(context, spanCount).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (position == documentAdapter.itemCount && footerAdapter.itemCount > 0) spanCount
                        else 1
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                pagingDataFlow
                    .collectLatest { pagingData ->
                        documentAdapter.submitData(pagingData)
                    }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                documentAdapter.loadStateFlow.collectLatest { loadState ->
                    val isListEmpty =
                        loadState.refresh is LoadState.NotLoading && documentAdapter.itemCount == 0
                    textEmpty.isVisible = isListEmpty
                    list.isVisible = !isListEmpty
                    textError.isVisible = loadState.source.refresh is LoadState.Error
                    swipeRefreshLayout.isRefreshing = loadState.source.refresh is LoadState.Loading
                    if (loadState.source.refresh is LoadState.Error) list.isVisible = false
                }
            }
        }

        swipeRefreshLayout.setOnRefreshListener {
            documentAdapter.refresh()
        }
    }
}