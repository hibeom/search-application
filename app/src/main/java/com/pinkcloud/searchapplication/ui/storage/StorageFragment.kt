package com.pinkcloud.searchapplication.ui.storage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.pinkcloud.searchapplication.R
import com.pinkcloud.searchapplication.databinding.StorageFragmentBinding
import com.pinkcloud.searchapplication.util.calculateSpanCount
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StorageFragment : Fragment() {

    private lateinit var binding: StorageFragmentBinding
    private val viewModel: StorageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = StorageFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.setList()

        return binding.root
    }

    private fun StorageFragmentBinding.setList() {
        val spanCount = calculateSpanCount(requireActivity())
        list.apply {
            adapter = ThumbnailAdapter()
            layoutManager = GridLayoutManager(context, spanCount)
        }
    }
}