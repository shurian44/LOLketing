package com.ezen.lolketing.view.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.databinding.ItemGalleryItemBinding
import com.ezen.lolketing.model.GalleryItem
import com.ezen.lolketing.util.setGlide

class GalleryAdapter(
    private val itemClickListener : (Int) -> Unit,
) : ListAdapter<GalleryItem, GalleryAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding : ItemGalleryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(galleryItem: GalleryItem) = with(binding) {

            root.setOnClickListener {
                itemClickListener(adapterPosition)
            }

            viewBackground.isVisible = galleryItem.isChecked

            checkBox.apply {
                isChecked = galleryItem.isChecked
                setOnCheckedChangeListener { _, isChecked ->
                    viewBackground.isVisible = isChecked
                    currentList[adapterPosition].isChecked = isChecked
                    if (isChecked) {
                        setOneSelect(adapterPosition)
                    }
                }
            }

            setGlide(imageView, galleryItem.contentUri)
        }
    }

    private fun setOneSelect(position: Int) {
        currentList.forEachIndexed { index, galleryItem ->
            if (galleryItem.isChecked && index != position) {
                galleryItem.isChecked = false
                notifyItemChanged(index)
            }
        }
    }

    fun getSelectItem() = currentList.firstOrNull { it.isChecked }

    fun setSelectItem(selectItem: GalleryItem?) {
        if (selectItem == null) {
            currentList.forEachIndexed { index, galleryItem ->
                if (galleryItem.isChecked) {
                    galleryItem.isChecked = false
                    notifyItemChanged(index)
                }
            }
            return
        }

        currentList.forEachIndexed { index, galleryItem ->
            if (selectItem.id == galleryItem.id) {
                galleryItem.isChecked = true
                notifyItemChanged(index)
            }

            if (galleryItem.isChecked && selectItem.id != galleryItem.id) {
                galleryItem.isChecked = false
                notifyItemChanged(index)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemGalleryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<GalleryItem>(){
            override fun areItemsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean = oldItem.contentUri == newItem.contentUri
        }
    }

}