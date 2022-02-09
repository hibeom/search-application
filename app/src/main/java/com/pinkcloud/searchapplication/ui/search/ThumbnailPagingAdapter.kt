package com.pinkcloud.searchapplication.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pinkcloud.domain.model.Thumbnail
import com.pinkcloud.searchapplication.databinding.ThumbnailItemLayoutBinding

class ThumbnailPagingAdapter(
    private val onClick: (Thumbnail) -> Unit
) : PagingDataAdapter<Thumbnail, ThumbnailPagingAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(private val binding: ThumbnailItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(thumbnail: Thumbnail, onClick: (Thumbnail) -> Unit) {
            binding.thumbnail = thumbnail
            binding.isSelected = thumbnail.isSelected
            binding.imageView.setOnClickListener {
                thumbnail.isSelected = !thumbnail.isSelected
                binding.isSelected = thumbnail.isSelected
                onClick(thumbnail)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ThumbnailItemLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { thumbnail ->
            holder.bind(thumbnail, onClick)
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<Thumbnail>() {
    override fun areItemsTheSame(oldItem: Thumbnail, newItem: Thumbnail): Boolean {
        return oldItem.thumbnailUrl == newItem.thumbnailUrl
    }

    override fun areContentsTheSame(oldItem: Thumbnail, newItem: Thumbnail): Boolean {
        return oldItem == newItem
    }
}