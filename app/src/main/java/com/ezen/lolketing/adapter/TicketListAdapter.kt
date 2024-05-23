package com.ezen.lolketing.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.databinding.ItemReserveTicketBinding
import com.ezen.lolketing.view.custom.TicketItem

class TicketListAdapter(
    private val onclick: (TicketItem) -> Unit
) : ListAdapter<TicketItem, ReserveHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReserveHolder =
        ReserveHolder(ItemReserveTicketBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(holder: ReserveHolder, position: Int) {
        holder.bind(
            currentList[position],
            onclick
        )
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<TicketItem>() {
            override fun areItemsTheSame(oldItem: TicketItem, newItem: TicketItem): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: TicketItem, newItem: TicketItem): Boolean =
                oldItem.id == newItem.id
        }
    }

}

class ReserveHolder(
    private val binding: ItemReserveTicketBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        item: TicketItem,
        onClick: (TicketItem) -> Unit
    ) = with(binding) {
        this.item = item

        ticketView.setOnClickListener {
            onClick(item)
        }
    }
}
