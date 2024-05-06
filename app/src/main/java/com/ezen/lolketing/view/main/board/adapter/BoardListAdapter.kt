package com.ezen.lolketing.view.main.board.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.databinding.ItemBoardBinding
import com.ezen.network.model.Board

class BoardListAdapter(
    private val onItemClick: (Int) -> Unit,
    private val onLikeClick: (Int) -> Unit,
    private val onMenuClick: (Int) -> Unit
) : ListAdapter<Board, BoardViewHolder>(diffUtil) {

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        holder.bind(
            currentList[position],
            onItemClick,
            onLikeClick,
            onMenuClick
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
        onItemClick: (Int) -> Unit,
        onLikeClick: (Int) -> Unit,
        onMenuClick: (Int) -> Unit
    ) = with(binding) {
        this.board = board

        root.setOnClickListener {
            onItemClick(board.id)
        }

        btnLike.setOnClickListener {
            onLikeClick(board.id)
        }

        btnMore.setOnClickListener {
            onMenuClick(board.id)
        }
    }
}