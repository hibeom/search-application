package com.pinkcloud.searchapplication.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pinkcloud.domain.model.Document
import com.pinkcloud.searchapplication.databinding.DocumentItemLayoutBinding

class DocumentPagingAdapter(
    private val onClick: (Document) -> Unit
) : PagingDataAdapter<Document, DocumentPagingAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(private val binding: DocumentItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(document: Document, onClick: (Document) -> Unit) {
            binding.document = document
            binding.isSelected = document.isSelected
            binding.imageView.setOnClickListener {
                document.isSelected = !document.isSelected
                binding.isSelected = document.isSelected
                onClick(document)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = DocumentItemLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { document ->
            holder.bind(document, onClick)
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<Document>() {
    override fun areItemsTheSame(oldItem: Document, newItem: Document): Boolean {
        return oldItem.thumbnailUrl == newItem.thumbnailUrl
    }

    override fun areContentsTheSame(oldItem: Document, newItem: Document): Boolean {
        return oldItem == newItem
    }
}