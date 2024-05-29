package com.ezen.lolketing.view.main.shop.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.databinding.ItemGoodsHistoryBinding
import com.ezen.lolketing.databinding.ItemPurchaseDateBinding
import com.ezen.network.model.PurchaseHistoryInfo

class PurchaseGoodsHistoryAdapter: ListAdapter<PurchaseHistoryInfo, RecyclerView.ViewHolder>(diffUtil) {

    override fun getItemViewType(position: Int): Int {
        return currentList[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when(viewType) {
        PurchaseHistoryInfo.DATE -> {
            PurchaseHistoryDateViewHolder(
                ItemPurchaseDateBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
        else -> {
            GoodsHistoryViewHolder(
                ItemGoodsHistoryBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is GoodsHistoryViewHolder -> {
                (currentList[position] as? PurchaseHistoryInfo.PurchaseGoodsHistory)?.let {
                    holder.bind(it)
                }
            }
            is PurchaseHistoryDateViewHolder -> {
                (currentList[position] as? PurchaseHistoryInfo.PurchaseHistoryDate)?.let {
                    holder.bind(it, true)
                }
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<PurchaseHistoryInfo>() {
            override fun areItemsTheSame(
                oldItem: PurchaseHistoryInfo,
                newItem: PurchaseHistoryInfo
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: PurchaseHistoryInfo,
                newItem: PurchaseHistoryInfo
            ) = oldItem == newItem
        }
    }
}

class GoodsHistoryViewHolder(
    private val binding: ItemGoodsHistoryBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(
        item: PurchaseHistoryInfo.PurchaseGoodsHistory
    ) {
        binding.item = item
    }
}