package com.ezen.lolketing.view.main.ticket.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.databinding.ItemHallBinding
import com.ezen.network.model.Seat

class HallAdapter(
    private val onClick: (String) -> Unit
) : ListAdapter<Seat, HallViewHolder>(diffUtil) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HallViewHolder =
        HallViewHolder(ItemHallBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: HallViewHolder, position: Int) {
        holder.bind(currentList[position], onClick)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Seat>() {
            override fun areItemsTheSame(oldItem: Seat, newItem: Seat) =
                oldItem.number == newItem.number

            override fun areContentsTheSame(oldItem: Seat, newItem: Seat) =
                oldItem == newItem
        }
    }
}

class HallViewHolder(
    private val binding: ItemHallBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        seat: Seat,
        onClick: (String) -> Unit
    ) = with(binding) {
        data = seat
        checkBox.setOnClick { onClick(seat.number) }
    }
}