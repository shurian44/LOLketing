package com.ezen.lolketing.view.main.ticket.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.databinding.ItemHallBinding
import com.ezen.lolketing.model.SeatItem

class HallAdapter(
    private val onClick: (SeatItem) -> Unit
) : ListAdapter<SeatItem, HallViewHolder>(diffUtil) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HallViewHolder =
        HallViewHolder(ItemHallBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: HallViewHolder, position: Int) {
        holder.bind(currentList[position], onClick)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<SeatItem>() {
            override fun areItemsTheSame(oldItem: SeatItem, newItem: SeatItem) =
                oldItem.documentId == newItem.documentId

            override fun areContentsTheSame(oldItem: SeatItem, newItem: SeatItem) =
                oldItem == newItem
        }
    }
}

class HallViewHolder(
    private val binding: ItemHallBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        seat: SeatItem,
        onClick: (SeatItem) -> Unit
    ) = with(binding) {
        data = seat
        checkBox.setOnClick { onClick(seat) }
    }
}