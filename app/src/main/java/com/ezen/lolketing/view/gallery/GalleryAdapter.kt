package com.ezen.lolketing.view.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ezen.lolketing.databinding.ItemGalleryItemBinding
import com.ezen.lolketing.model.GalleryItem
import com.ezen.lolketing.util.setGlide
import java.util.ArrayList

class GalleryAdapter(
    private val itemClickListener : (Int) -> Unit,
    private val itemCountListener : (Int) -> Unit
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

                    itemCountListener(getSelectItemList().size)
                }
            }

            setGlide(imageView, galleryItem.contentUri)
        }
    }

    fun getSelectItemList() = currentList.filter { it.isChecked }

    fun setSelectItemList(list: ArrayList<GalleryItem>?) {
        list?.forEach { item ->
            val index = currentList.indexOfFirst { it.id == item.id }
            if (currentList[index].isChecked != item.isChecked){
                currentList[index].isChecked = item.isChecked
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