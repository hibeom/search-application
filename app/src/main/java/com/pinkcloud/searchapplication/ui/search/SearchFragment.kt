package com.pinkcloud.searchapplication.ui.search

import android.os.Bundle
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
import androidx.recyclerview.widget.RecyclerView
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
            },
        )
        binding.setOnSaveAction(
            isSaveCompleted = viewModel.isSaveCompleted,
            onSave = viewModel::resetSaveCompleted
        )

        return binding.root
    }

    private fun SearchFragmentBinding.setSearchInputListener(
        onSearch: (String) -> Unit
    ) {
        searchTextInput.apply {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard(context, this)
                    clearFocus()
                    searchInput(onSearch)
                    true
                } else false
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
        onClickDocument: (Document) -> Unit,
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
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    pagingDataFlow
                        .collectLatest { pagingData ->
                            documentAdapter.submitData(pagingData)
                        }
                }
                launch {
                    documentAdapter.loadStateFlow.collectLatest { loadState ->
                        pagingLoadState = loadState
                        isListEmpty =
                            loadState.refresh is LoadState.NotLoading && documentAdapter.itemCount == 0
                    }
                }
            }
        }

        swipeRefreshLayout.setOnRefreshListener {
            documentAdapter.refresh()
        }
    }

    private fun SearchFragmentBinding.setOnSaveAction(
        isSaveCompleted: Flow<Boolean>,
        onSave: () -> Unit
    ) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    isSaveCompleted.collect { isSaveCompleted ->
                        if (isSaveCompleted) {
                            resetVisibleViewHolder(list)
                            onSave()
                        }
                    }
                }
            }
        }
    }

    private fun resetVisibleViewHolder(
        list: RecyclerView
    ) {
        val lm = list.layoutManager as GridLayoutManager
        val first = lm.findFirstVisibleItemPosition()
        val last = lm.findLastVisibleItemPosition()
        for (position in first..last) {
            (list.findViewHolderForLayoutPosition(position) as DocumentPagingAdapter.ViewHolder).reset()
        }
    }
}