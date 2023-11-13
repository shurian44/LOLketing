package com.ezen.lolketing.view.custom.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ezen.lolketing.databinding.CellImageBinding

class ImageSliderAdapter: ListAdapter<Int, ImageSliderViewHolder>(diffUtil) {
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

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Int>() {
            override fun areItemsTheSame(oldItem: Int, newItem: Int) = oldItem == newItem

            override fun areContentsTheSame(oldItem: Int, newItem: Int) = oldItem == newItem
        }
    }

}

class ImageSliderViewHolder(
    private val binding: CellImageBinding
): ViewHolder(binding.root) {
    fun bind(item: Int) {
        binding.res = item
    }
}