package com.ezen.lolketing.view.main.ticket.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.databinding.ItemHallBinding
import com.ezen.lolketing.model.SeatItem

class HallAdapter(
    val checkedChangeListener : (Int, SeatItem) -> Unit
) : RecyclerView.Adapter<HallAdapter.ViewHolder>() {

    private var list = listOf<SeatItem>()

    inner class ViewHolder(private val binding: ItemHallBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(seat: SeatItem) = with(binding) {
            data = seat
            checkBox.isEnabled = seat.enabled
            checkBox.isChecked = seat.checked
            checkBox.setOnChangeListener {
                seat.checked = it
                checkedChangeListener(adapterPosition, seat)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemHallBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun setSeatList(list: List<SeatItem>) {
        this.list = list
    }

    fun getSeatList() = list

    fun setChecked(position: Int, checked: Boolean) {
        list[position].checked = checked
        notifyItemChanged(position)
    }

}