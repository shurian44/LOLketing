package com.ezen.lolketing.view.main.board.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ItemBoardBinding
import com.ezen.lolketing.databinding.ItemBoardTopBinding
import com.ezen.lolketing.model.BoardItem
import com.ezen.lolketing.model.BoardItem.Companion.TYPE_TEAM_BOARD
import com.ezen.lolketing.util.getBoardImage

class BoardListAdapter(
    val onclickListener : (String) -> Unit,
) : ListAdapter<BoardItem, RecyclerView.ViewHolder>(diffUtil) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TopImageViewHolder) {
            holder.bind(currentList[position] as BoardItem.TeamImage)
        }

        if (holder is BoardListViewHolder) {
            holder.bind(
                currentList[position] as BoardItem.BoardListItem,
                onclickListener
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_TEAM_BOARD) {
            BoardListViewHolder(ItemBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            TopImageViewHolder(ItemBoardTopBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return currentList[position].type
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<BoardItem>() {
            override fun areItemsTheSame(oldItem: BoardItem, newItem: BoardItem) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: BoardItem, newItem: BoardItem) =
                oldItem == newItem
        }
    }
}

class TopImageViewHolder(val binding: ItemBoardTopBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        teamImage : BoardItem.TeamImage
    ) = with(binding) {
        imageView.setImageResource(getBoardImage(teamImage.team))
    }
}

class BoardListViewHolder(val binding : ItemBoardBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        board: BoardItem.BoardListItem,
        onclickListener : (String) -> Unit
    ) = with(binding) {
        this.board = board
        if (absoluteAdapterPosition % 2 == 0) {
            root.setBackgroundResource(R.color.black)
        } else {
            root.setBackgroundResource(R.color.light_black)
        }

        root.setOnClickListener {
            onclickListener(board.documentId)
        }
    }
}