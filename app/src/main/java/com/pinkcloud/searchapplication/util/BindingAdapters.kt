package com.pinkcloud.searchapplication.util

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pinkcloud.domain.model.Thumbnail
import com.pinkcloud.domain.util.Result
import com.pinkcloud.searchapplication.R
import com.pinkcloud.searchapplication.ui.search.ThumbnailPagingAdapter
import com.pinkcloud.searchapplication.ui.storage.ThumbnailAdapter

@BindingAdapter("image")
fun setImage(imageView: ImageView, image: Thumbnail) {
    image.run {
        Glide.with(imageView)
            .load(thumbnailUrl)
            .placeholder(R.color.gray)
            .centerCrop()
            .into(imageView)
    }
}

@BindingAdapter("selectedThumbnails")
fun Button.setEnableBySelectedThumbnailsSize(thumbnails: Map<String, Thumbnail>) {
    isEnabled = thumbnails.isNotEmpty()
}

@BindingAdapter("items")
fun setItems(list: RecyclerView, thumbnails: List<Thumbnail>) {
    val adapter = list.adapter as? ThumbnailAdapter
    adapter?.let {
        adapter.submitList(thumbnails)
    }
}

@BindingAdapter("emptyVisibility")
fun setEmptyVisibility(textView: TextView, thumbnails: List<Thumbnail>) {
    textView.isVisible = thumbnails.isEmpty()
}