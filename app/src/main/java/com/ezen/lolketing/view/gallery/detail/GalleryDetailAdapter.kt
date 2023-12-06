package com.ezen.lolketing.view.gallery.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.databinding.ItemGalleryDetailItemBinding
import com.ezen.lolketing.model.GalleryItem
import com.ezen.lolketing.util.setGlide

class GalleryDetailAdapter(
    private val onClick : () -> Unit
) : ListAdapter<GalleryItem, GalleryDetailViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryDetailViewHolder =
        GalleryDetailViewHolder(
            ItemGalleryDetailItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: GalleryDetailViewHolder, position: Int) {
        holder.bind(currentList[position], onClick)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<GalleryItem>() {
            override fun areItemsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean =
                oldItem.contentUri == newItem.contentUri

            override fun areContentsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean =
                oldItem == newItem
        }
    }
}

class GalleryDetailViewHolder(
    private val binding: ItemGalleryDetailItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        galleryItem: GalleryItem,
        onClick: () -> Unit
    ) {

        setGlide(binding.imageView, galleryItem.contentUri)
        binding.imageView.setOnClickListener {
            onClick()
        }
    }
}