package com.ezen.lolketing.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.databinding.ItemLeftTeamChatBinding
import com.ezen.lolketing.databinding.ItemRightTeamChatBinding
import com.ezen.lolketing.model.ChattingItem

class ChattingAdapter(
    private val leftTeam: String,
): ListAdapter<ChattingItem, RecyclerView.ViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            0 -> {
                LeftTeamChattingViewHolder(
                    ItemLeftTeamChatBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
            else -> {
                RightTeamChattingViewHolder(
                    ItemRightTeamChatBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is LeftTeamChattingViewHolder -> {
                holder.bind(currentList[position])
            }
            is RightTeamChattingViewHolder -> {
                holder.bind(currentList[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (currentList[position].team == leftTeam) 0 else 1
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ChattingItem>() {
            override fun areItemsTheSame(oldItem: ChattingItem, newItem: ChattingItem) =
                oldItem.id == newItem.id && oldItem.timestamp == newItem.timestamp

            override fun areContentsTheSame(oldItem: ChattingItem, newItem: ChattingItem) =
                oldItem == newItem
        }
    }

}

class LeftTeamChattingViewHolder(
    private val binding: ItemLeftTeamChatBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ChattingItem) {
        binding.item = item
    }
}

class RightTeamChattingViewHolder(
    private val binding: ItemRightTeamChatBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ChattingItem) {
        binding.item = item
    }
}