package com.pinkcloud.searchapplication.util

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.pinkcloud.domain.model.Document
import com.pinkcloud.searchapplication.R
import com.pinkcloud.searchapplication.ui.storage.DocumentAdapter

@BindingAdapter("image")
fun setImage(imageView: ImageView, document: Document) {
    document.run {
        Glide.with(imageView)
            .load(thumbnailUrl)
            .placeholder(R.color.gray)
            .centerCrop()
            .into(imageView)
    }
}

@BindingAdapter("selectedDocuments")
fun Button.setEnableBySelectedDocumentsSize(documents: Map<String, Document>) {
    isEnabled = documents.isNotEmpty()
}

@BindingAdapter("items")
fun setItems(list: RecyclerView, documents: List<Document>) {
    val adapter = list.adapter as? DocumentAdapter
    adapter?.let {
        adapter.submitList(documents)
    }
}

@BindingAdapter("emptyVisibility")
fun setEmptyVisibility(textView: TextView, documents: List<Document>) {
    textView.isVisible = documents.isEmpty()
}

@BindingAdapter("loadState")
fun setRefreshing(refreshLayout: SwipeRefreshLayout, loadState: CombinedLoadStates?) {
    refreshLayout.isRefreshing = loadState?.source?.refresh is LoadState.Loading
}

@BindingAdapter("loadState", "isEmpty")
fun setPagingRecyclerviewVisibility(
    list: RecyclerView,
    loadState: CombinedLoadStates?,
    isEmpty: Boolean
) {
    list.isVisible = loadState?.source?.refresh !is LoadState.Error && !isEmpty
}