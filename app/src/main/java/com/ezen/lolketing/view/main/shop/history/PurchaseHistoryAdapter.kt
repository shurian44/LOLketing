package com.ezen.lolketing.view.main.shop.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.databinding.ItemPurchaseDateBinding
import com.ezen.lolketing.databinding.ItemPurchaseHistoryBinding
import com.ezen.lolketing.model.PurchaseHistory

class PurchaseHistoryAdapter(
    private val onClick: (String) -> Unit
): ListAdapter<PurchaseHistory, RecyclerView.ViewHolder>(diffUtil) {

    override fun getItemViewType(position: Int): Int {
        return currentList[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when(viewType) {
        PurchaseHistory.DATE -> {
            PurchaseHistoryDateViewHolder(
                ItemPurchaseDateBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
        else -> {
            PurchaseHistoryViewHolder(
                ItemPurchaseHistoryBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is PurchaseHistoryViewHolder -> {
                (currentList[position] as? PurchaseHistory.History)?.let {
                    holder.bind(it, onClick)
                }
            }
            is PurchaseHistoryDateViewHolder -> {
                (currentList[position] as? PurchaseHistory.PurchaseDate)?.let {
                    holder.bind(it)
                }
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<PurchaseHistory>() {
            override fun areItemsTheSame(
                oldItem: PurchaseHistory,
                newItem: PurchaseHistory
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: PurchaseHistory,
                newItem: PurchaseHistory
            ) = oldItem == newItem
        }
    }
}

class PurchaseHistoryViewHolder(
    private val binding: ItemPurchaseHistoryBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(
        item: PurchaseHistory.History,
        onClick: (String) -> Unit
    ) {
        binding.item = item
        binding.root.setOnClickListener { onClick(item.documentId) }
    }
}

class PurchaseHistoryDateViewHolder(
    private val binding: ItemPurchaseDateBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: PurchaseHistory.PurchaseDate) {
        binding.date = item.date
    }
}