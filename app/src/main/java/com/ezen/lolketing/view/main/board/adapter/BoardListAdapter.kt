package com.ezen.lolketing.view.main.board.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.databinding.ItemBoardBinding
import com.ezen.network.model.Board

class BoardListAdapter(
    private val onClickListener : (Int) -> Unit,
) : ListAdapter<Board, BoardViewHolder>(diffUtil) {

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        holder.bind(
            currentList[position],
            onClickListener
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder =
        BoardViewHolder(
            ItemBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Board>() {
            override fun areItemsTheSame(oldItem: Board, newItem: Board) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Board, newItem: Board) =
                oldItem == newItem
        }
    }
}

class BoardViewHolder(val binding : ItemBoardBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        board: Board,
        onClickListener : (Int) -> Unit
    ) = with(binding) {
        this.board = board

        root.setOnClickListener {
            onClickListener(board.id)
        }
    }
}