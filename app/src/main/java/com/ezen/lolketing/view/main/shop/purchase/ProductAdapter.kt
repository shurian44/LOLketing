package com.ezen.lolketing.view.main.shop.purchase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.databinding.ItemProductBinding
import com.ezen.lolketing.model.PurchaseInfo

class ProductAdapter(
    private val isBasket: Boolean,
    private val onClick: (Long) -> Unit
) : ListAdapter<PurchaseInfo, ProductViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ProductViewHolder(
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(
            currentList[position],
            isBasket,
            onClick
        )
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<PurchaseInfo>() {
            override fun areItemsTheSame(oldItem: PurchaseInfo, newItem: PurchaseInfo) =
                oldItem.name == newItem.name && oldItem.timestamp == newItem.timestamp

            override fun areContentsTheSame(oldItem: PurchaseInfo, newItem: PurchaseInfo) =
                oldItem == newItem
        }
    }
}

class ProductViewHolder(
    private val binding: ItemProductBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: PurchaseInfo,
        isBasket: Boolean,
        onClick: (Long) -> Unit
    ) {
        binding.isBasket = isBasket
        binding.item = item
        binding.checkbox.setOnClickListener {
            if (isBasket) onClick(item.databaseId)
        }
    }

}