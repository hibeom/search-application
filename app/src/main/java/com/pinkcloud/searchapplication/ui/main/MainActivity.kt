package com.pinkcloud.searchapplication.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.pinkcloud.searchapplication.R
import com.pinkcloud.searchapplication.databinding.MainActivityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pagerAdapter = PagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = when(position) {
                0 -> getString(R.string.title_search)
                1 -> getString(R.string.title_storage)
                else -> getString(R.string.title_search)
            }
        }.attach()
    }
}