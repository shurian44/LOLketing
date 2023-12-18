package com.ezen.lolketing.view.custom.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ezen.lolketing.databinding.CellImageBinding
import com.ezen.lolketing.util.setGlide

class ImageSliderAdapter: ListAdapter<Any, ImageSliderViewHolder>(BaseItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ImageSliderViewHolder(
            CellImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ImageSliderViewHolder, position: Int) {
        holder.bind(item = currentList[position])
    }

}

class ImageSliderViewHolder(
    private val binding: CellImageBinding
): ViewHolder(binding.root) {
    fun bind(item: Any) {
        if (item is Int) {
            binding.imageView.setImageResource(item)
        } else if (item is String) {
            setGlide(binding.imageView, item)
        }
    }
}

class BaseItemCallback : DiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any) = oldItem.toString() == newItem.toString()

    override fun areContentsTheSame(oldItem: Any, newItem: Any) = true
}