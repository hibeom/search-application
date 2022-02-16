package com.pinkcloud.searchapplication.ui.storage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pinkcloud.searchapplication.R
import com.pinkcloud.searchapplication.databinding.StorageFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StorageFragment : Fragment() {

    private lateinit var binding: StorageFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = StorageFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }
}