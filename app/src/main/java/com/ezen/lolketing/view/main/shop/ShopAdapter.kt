package com.ezen.lolketing.view.main.shop

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.databinding.ItemShoppingBinding
import com.ezen.lolketing.model.ShopListItem

class ShopAdapter(
    private val onClick: (String) -> Unit
): ListAdapter<ShopListItem, ShopViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ShopViewHolder(
            ItemShoppingBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        holder.bind(
            currentList[position],
            onClick
        )
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ShopListItem>() {
            override fun areItemsTheSame(oldItem: ShopListItem, newItem: ShopListItem) =
                oldItem.documentId == newItem.documentId

            override fun areContentsTheSame(oldItem: ShopListItem, newItem: ShopListItem) =
                oldItem == newItem

        }
    }
}

class ShopViewHolder(
    private val binding: ItemShoppingBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(
        item: ShopListItem,
        onClick: (String) -> Unit
    ) {
        binding.item = item
        binding.root.setOnClickListener { onClick(item.documentId) }
    }
}