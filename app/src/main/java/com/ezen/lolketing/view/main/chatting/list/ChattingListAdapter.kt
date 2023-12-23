package com.ezen.lolketing.view.main.chatting.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.databinding.ItemChattingBinding
import com.ezen.lolketing.model.ChattingInfo

class ChattingListAdapter(
    private val onClick: (ChattingInfo) -> Unit
) : ListAdapter<ChattingInfo, ChattingListViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ChattingListViewHolder(
        ItemChattingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: ChattingListViewHolder, position: Int) {
        holder.bind(currentList[position], onClick)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ChattingInfo>() {
            override fun areItemsTheSame(oldItem: ChattingInfo, newItem: ChattingInfo) =
                oldItem.time == newItem.time && "${oldItem.leftTeam}:${oldItem.rightTeam}" == "${newItem.leftTeam}:${newItem.rightTeam}"

            override fun areContentsTheSame(oldItem: ChattingInfo, newItem: ChattingInfo) =
                oldItem == newItem
        }
    }
}

class ChattingListViewHolder(
    private val binding: ItemChattingBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: ChattingInfo,
        onClick: (ChattingInfo) -> Unit
    ) = with(binding) {
        this.item = item
        imgTeamLeft.setOnClickListener {
            onClick(item.copy(selectTeam = item.leftTeam))
        }
        imgTeamRight.setOnClickListener {
            onClick(item.copy(selectTeam = item.rightTeam))
        }
    }

}