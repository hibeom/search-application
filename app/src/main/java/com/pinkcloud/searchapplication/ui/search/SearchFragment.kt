package com.pinkcloud.searchapplication.ui.search

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pinkcloud.domain.model.Thumbnail
import com.pinkcloud.domain.util.Result
import com.pinkcloud.searchapplication.databinding.SearchFragmentBinding
import com.pinkcloud.searchapplication.util.calculateSpanCount
import com.pinkcloud.searchapplication.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
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
        binding.setSearchResult()

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
    ) {
        val spanCount = calculateSpanCount(requireActivity())

        list.apply {
            adapter = ThumbnailAdapter()
            layoutManager = GridLayoutManager(context, spanCount)
        }
    }
}