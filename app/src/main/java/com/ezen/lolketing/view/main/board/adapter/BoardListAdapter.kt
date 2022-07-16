package com.ezen.lolketing.view.main.board.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ItemBoardBinding
import com.ezen.lolketing.databinding.ItemBoardTopBinding
import com.ezen.lolketing.model.BoardItem
import com.ezen.lolketing.model.BoardItem.Companion.TYPE_TEAM_BOARD
import com.ezen.lolketing.util.getBoardImage

class BoardListAdapter(
    val onclickListener : (String) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list = mutableListOf<BoardItem>()

    inner class TopImageViewHolder(val binding: ItemBoardTopBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(teamImage : BoardItem.TeamImage) = with(binding) {
            imageView.setImageResource(getBoardImage(teamImage.team))
        }
    }

    inner class BoardListViewHolder(val binding : ItemBoardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(board: BoardItem.BoardListItem) = with(binding) {
            this.board = board
            if (adapterPosition % 2 == 0) {
                root.setBackgroundResource(R.color.black)
            } else {
                root.setBackgroundResource(R.color.light_black)
            }

            root.setOnClickListener {
                board.documentId?.let { id ->
                    onclickListener(id)
                }
            }
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TopImageViewHolder) {
            holder.bind(list[position] as BoardItem.TeamImage)
        }

        if (holder is BoardListViewHolder) {
            holder.bind(list[position] as BoardItem.BoardListItem)
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
        return list[position].type
    }



    fun addItem(item : BoardItem) {
        list.add(item)
        notifyItemChanged(list.lastIndex)
    }

    fun addItemList(list : List<BoardItem>) {
        list.forEach {
            this.list.add(it)
            notifyItemChanged(list.lastIndex)
        }
    }

}