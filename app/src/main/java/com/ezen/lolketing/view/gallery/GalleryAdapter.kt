package com.ezen.lolketing.view.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.databinding.ItemGalleryItemBinding
import com.ezen.lolketing.model.GalleryItem

class GalleryAdapter(
    private val onCheckedClick : (Int) -> Unit,
    private val onItemClick: (Int) -> Unit
) : ListAdapter<GalleryItem, ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemGalleryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            galleryItem = currentList[position],
            onCheckedClick = onCheckedClick,
            onItemClick = onItemClick
        )
    }

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<GalleryItem>(){
            override fun areItemsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean = oldItem.contentUri == newItem.contentUri
        }
    }

}

class ViewHolder(private val binding : ItemGalleryItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        galleryItem: GalleryItem,
        onCheckedClick: (Int) -> Unit,
        onItemClick: (Int) -> Unit
    ) {
        binding.item = galleryItem
        binding.root.setOnClickListener { onItemClick(absoluteAdapterPosition) }
        binding.checkbox.setOnClickListener { onCheckedClick(absoluteAdapterPosition) }
    }
}