package com.ezen.lolketing.view.main.shop

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.databinding.ItemShoppingBinding
import com.ezen.network.model.Goods

class ShopAdapter(
    private val onClick: (Int) -> Unit
): ListAdapter<Goods, ShopViewHolder>(diffUtil) {

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
        val diffUtil = object : DiffUtil.ItemCallback<Goods>() {
            override fun areItemsTheSame(oldItem: Goods, newItem: Goods) =
                oldItem.goodsId == newItem.goodsId

            override fun areContentsTheSame(oldItem: Goods, newItem: Goods) =
                oldItem == newItem

        }
    }
}

class ShopViewHolder(
    private val binding: ItemShoppingBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(
        item: Goods,
        onClick: (Int) -> Unit
    ) {
        binding.item = item
        binding.root.setOnClickListener { onClick(item.goodsId) }
    }
}