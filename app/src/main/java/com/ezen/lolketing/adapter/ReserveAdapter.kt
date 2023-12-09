package com.ezen.lolketing.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ItemReserveTicketBinding
import com.ezen.lolketing.model.Ticket

class ReserveAdapter(
    private val onclick: (Ticket) -> Unit
) : ListAdapter<Ticket, ReserveHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReserveHolder =
        ReserveHolder(ItemReserveTicketBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(holder: ReserveHolder, position: Int) {
        holder.bind(
            currentList[position],
            onclick
        )
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Ticket>() {
            override fun areItemsTheSame(oldItem: Ticket, newItem: Ticket): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Ticket, newItem: Ticket): Boolean =
                oldItem.getTicketTime() == newItem.getTicketTime()
        }
    }


}

class ReserveHolder(
    private val binding: ItemReserveTicketBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        item: Ticket,
        onclick: (Ticket) -> Unit
    ) = with(binding) {
        ticket = item

        // 배경색 포지션 값에 따라 변경
        if (absoluteAdapterPosition % 2 == 0) {
            root.setBackgroundResource(R.color.black)
        } else {
            root.setBackgroundResource(R.color.light_black)
        }

        root.setOnClickListener {
            onclick(item)
        }
    }
}
