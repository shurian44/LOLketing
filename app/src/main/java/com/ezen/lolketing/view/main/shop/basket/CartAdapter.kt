package com.ezen.lolketing.view.main.shop.basket

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezen.database.entity.GoodsEntity
import com.ezen.lolketing.databinding.ItemCartBinding

class CartAdapter(
    private val isBasket: Boolean,
    private val onClick: (Int) -> Unit,
    private val onDecreaseAmount: (Int) -> Unit,
    private val onIncreaseAmount: (Int) -> Unit,
) : ListAdapter<GoodsEntity, CartViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CartViewHolder(
            ItemCartBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(
            currentList[position],
            isBasket,
            onClick,
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

class CartViewHolder(
    private val binding: ItemCartBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: GoodsEntity,
        isBasket: Boolean,
        onClick: (Int) -> Unit,
        decreaseAmount: (Int) -> Unit,
        increaseAmount: (Int) -> Unit
    ) = with(binding) {
        this.isBasket = isBasket
        this.item = item
        checkbox.setOnClickListener {
            if (isBasket) onClick(item.index)
        }
        btnMinus.setOnClickListener { decreaseAmount(item.index) }
        btnPlus.setOnClickListener { increaseAmount(item.index) }
    }

}