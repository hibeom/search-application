package com.pinkcloud.searchapplication.ui.storage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pinkcloud.domain.model.Thumbnail
import com.pinkcloud.searchapplication.databinding.ThumbnailItemLayoutBinding

class ThumbnailAdapter: ListAdapter<Thumbnail, ThumbnailAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(private val binding: ThumbnailItemLayoutBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(thumbnail: Thumbnail) {
            binding.thumbnail = thumbnail
            binding.isSelected = false
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
        holder.bind(getItem(position))
    }
}

class DiffCallback: DiffUtil.ItemCallback<Thumbnail>() {
    override fun areItemsTheSame(oldItem: Thumbnail, newItem: Thumbnail): Boolean {
        return oldItem.thumbnailUrl == newItem.thumbnailUrl
    }

    override fun areContentsTheSame(oldItem: Thumbnail, newItem: Thumbnail): Boolean {
        return oldItem == newItem
    }
}