package com.pinkcloud.searchapplication.ui.main

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.test.core.app.ActivityScenario
import com.pinkcloud.searchapplication.ui.search.SearchFragment
import com.pinkcloud.searchapplication.ui.storage.StorageFragment
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PagerAdapterTest {

    private lateinit var pagerAdapter: PagerAdapter

    @Before
    fun setup() {
        val activity = AppCompatActivity()
        pagerAdapter = PagerAdapter(activity)
    }

    @Test
    fun createFragment_firstPosition_isSearchFragment() {
        val fragment = pagerAdapter.createFragment(0)
        assertTrue(fragment is SearchFragment)
    }

    @Test
    fun createFragment_secondPosition_isStorageFragment() {
        val fragment = pagerAdapter.createFragment(1)
        assertTrue(fragment is StorageFragment)
    }
}