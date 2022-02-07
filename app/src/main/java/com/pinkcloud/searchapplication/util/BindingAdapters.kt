package com.pinkcloud.searchapplication.util

import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pinkcloud.domain.model.Thumbnail
import com.pinkcloud.domain.util.Result
import com.pinkcloud.searchapplication.R
import com.pinkcloud.searchapplication.ui.search.ThumbnailAdapter

@BindingAdapter("image")
fun setImage(imageView: ImageView, image: Thumbnail) {
    image.run {
        Glide.with(imageView.context)
            .load(thumbnailUrl)
            .placeholder(R.color.gray)
            .centerCrop()
            .into(imageView)
    }
}

@BindingAdapter("items")
fun setItems(list: RecyclerView, thumbnailsState: Result<List<Thumbnail>>) {
    val adapter = list.adapter as? ThumbnailAdapter
    if (thumbnailsState is Result.Success) {
        adapter?.submitList(thumbnailsState.data)
    }
}

@BindingAdapter("errorVisibility")
fun setErrorVisibility(textView: TextView, thumbnailsState: Result<List<Thumbnail>>) {
    textView.isVisible = thumbnailsState is Result.Error
}

@BindingAdapter("emptyVisibility")
fun setEmptyVisibility(textView: TextView, thumbnailsState: Result<List<Thumbnail>>) {
    textView.isVisible = (thumbnailsState is Result.Success) && (thumbnailsState.data!!.isEmpty())
}

@BindingAdapter("loadingVisibility")
fun setLoadingVisibility(progressbar: ProgressBar, thumbnailsState: Result<List<Thumbnail>>) {
    progressbar.isVisible = thumbnailsState is Result.Loading
}