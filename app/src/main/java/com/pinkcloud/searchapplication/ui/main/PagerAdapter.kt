package com.pinkcloud.searchapplication.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pinkcloud.searchapplication.ui.search.SearchFragment
import com.pinkcloud.searchapplication.ui.storage.StorageFragment

const val ARG_POSITION = "position"

class PagerAdapter(fragment: FragmentActivity): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment = when(position) {
            0 -> SearchFragment()
            1 -> StorageFragment()
            else -> SearchFragment()
        }
        fragment.arguments = Bundle().apply {
            putInt(ARG_POSITION, position)
        }
        return fragment
    }

}