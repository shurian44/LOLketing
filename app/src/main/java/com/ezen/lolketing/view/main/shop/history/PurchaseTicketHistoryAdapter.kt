package com.ezen.lolketing.view.main.shop.history

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.databinding.ItemPurchaseDateBinding
import com.ezen.lolketing.databinding.ItemReserveTicketBinding
import com.ezen.lolketing.util.densityDp
import com.ezen.lolketing.view.custom.TicketItem
import com.ezen.network.model.PurchaseHistoryInfo

class PurchaseTicketHistoryAdapter(
    private val onClick: (String) -> Unit
): ListAdapter<PurchaseHistoryInfo, RecyclerView.ViewHolder>(diffUtil) {

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
            TicketHistoryViewHolder(
                ItemReserveTicketBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is TicketHistoryViewHolder -> {
                (currentList[position] as? PurchaseHistoryInfo.PurchaseTicketHistory)?.let {
                    holder.bind(it, onClick)
                }
            }
            is PurchaseHistoryDateViewHolder -> {
                (currentList[position] as? PurchaseHistoryInfo.PurchaseHistoryDate)?.let {
                    holder.bind(it)
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

class TicketHistoryViewHolder(
    private val binding: ItemReserveTicketBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(
        item: PurchaseHistoryInfo.PurchaseTicketHistory,
        onClick: (String) -> Unit
    ) {
        binding.item = item.toTicketItem()
        binding.root.setOnClickListener { onClick(item.reservationIds) }
    }

    private fun PurchaseHistoryInfo.PurchaseTicketHistory.toTicketItem() = TicketItem(
        id = reservationIds,
        leftTeam = leftTeam,
        rightTeam = rightTeam,
        status = TicketItem.POSSIBLE,
        message = seatNumbers
    )
}

class PurchaseHistoryDateViewHolder(
    private val binding: ItemPurchaseDateBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(
        item: PurchaseHistoryInfo.PurchaseHistoryDate,
        isGoods: Boolean = false
    ) {
        binding.date = item.date

        if (isGoods) {
            val layoutParams = binding.txtDate.layoutParams as LinearLayout.LayoutParams
            layoutParams.marginStart = densityDp(binding.txtDate.context, 20)
            layoutParams.bottomMargin = densityDp(binding.txtDate.context, 5)

            if (bindingAdapterPosition != 0) {
                layoutParams.topMargin = densityDp(binding.txtDate.context, 15)
            }

            binding.txtDate.layoutParams = layoutParams
        }
    }
}