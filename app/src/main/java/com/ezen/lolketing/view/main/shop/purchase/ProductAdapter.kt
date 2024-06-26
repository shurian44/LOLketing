package com.ezen.lolketing.view.main.shop.purchase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezen.database.entity.GoodsEntity
import com.ezen.lolketing.databinding.ItemProductBinding

class ProductAdapter(
    private val isBasket: Boolean,
    private val onDelete: (Int) -> Unit,
    private val onDecreaseAmount: (Int) -> Unit,
    private val onIncreaseAmount: (Int) -> Unit,
) : ListAdapter<GoodsEntity, ProductViewHolder>(diffUtil) {
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
            onDelete,
            onDecreaseAmount,
            onIncreaseAmount
        )
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<GoodsEntity>() {
            override fun areItemsTheSame(oldItem: GoodsEntity, newItem: GoodsEntity) =
                oldItem.index == newItem.index

            override fun areContentsTheSame(oldItem: GoodsEntity, newItem: GoodsEntity) =
                oldItem == newItem
        }
    }
}

class ProductViewHolder(
    private val binding: ItemProductBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: GoodsEntity,
        isBasket: Boolean,
        onDelete: (Int) -> Unit,
        decreaseAmount: (Int) -> Unit,
        increaseAmount: (Int) -> Unit
    ) = with(binding) {
        this.isBasket = isBasket
        this.item = item
        btnDelete.setOnClickListener { onDelete(item.index) }
        btnMinus.setOnClickListener { decreaseAmount(item.index) }
        btnPlus.setOnClickListener { increaseAmount(item.index) }
    }

}