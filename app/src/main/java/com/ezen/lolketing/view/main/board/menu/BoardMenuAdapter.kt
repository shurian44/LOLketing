package com.ezen.lolketing.view.main.board.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.databinding.ItemBoardMenuBinding

class BoardMenuAdapter : ListAdapter<BoardMenuItem, BoardMenuViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardMenuViewHolder =
        BoardMenuViewHolder(
            ItemBoardMenuBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: BoardMenuViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<BoardMenuItem>() {
            override fun areItemsTheSame(oldItem: BoardMenuItem, newItem: BoardMenuItem) =
                oldItem.text == newItem.text

            override fun areContentsTheSame(
                oldItem: BoardMenuItem,
                newItem: BoardMenuItem
            ) = oldItem == newItem
        }
    }

}

class BoardMenuViewHolder(
    private val binding: ItemBoardMenuBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: BoardMenuItem) {
        binding.txtTitle.text = item.text
        binding.root.setOnClickListener { item.onClick() }

        binding.viewDivider.isVisible = bindingAdapterPosition != 0
    }
}

data class BoardMenuItem(
    val text: String,
    val onClick: () -> Unit
)