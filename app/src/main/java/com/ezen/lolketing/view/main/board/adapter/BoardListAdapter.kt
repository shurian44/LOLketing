package com.ezen.lolketing.view.main.board.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ItemBoardBinding
import com.ezen.lolketing.databinding.ItemBoardTopBinding
import com.ezen.lolketing.model.Board
import com.ezen.lolketing.model.Board.Companion.TYPE_TEAM_BOARD

class BoardListAdapter(
    val onclickListener : (String) -> Unit,
    val onLongClickListener: (Board.BoardItem.BoardListItem, View) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list = mutableListOf<Board.BoardItem>()

    inner class TopImageViewHolder(val binding: ItemBoardTopBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(teamImage : Board.BoardItem.TeamImage) = with(binding) {
            // todo image 팀별 수정 필요
            imageView.setImageResource(R.drawable.img_board_t1)
        }
    }

    inner class BoardListViewHolder(val binding : ItemBoardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(board: Board.BoardItem.BoardListItem) = with(binding) {
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

            root.setOnLongClickListener {
                onLongClickListener(board, it)
                true
            }
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TopImageViewHolder) {
            holder.bind(list[position] as Board.BoardItem.TeamImage)
        }

        if (holder is BoardListViewHolder) {
            holder.bind(list[position] as Board.BoardItem.BoardListItem)
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

    fun addItem(item : Board.BoardItem) {
        list.add(item)
        notifyItemChanged(list.lastIndex)
    }

    fun addItemList(list : List<Board.BoardItem>) {
        list.forEach {
            this.list.add(it)
            notifyItemChanged(list.lastIndex)
        }
    }

}


//
//    // 글 삭제 메소드
//    public void CommentDelete(final Context context, final int position) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//
//        builder.setTitle("글 삭제").setMessage("정말 삭제하시겠습니까?");
//        builder.setPositiveButton("네", new DialogInterface.OnClickListener(){
//            @Override
//            public void onClick(DialogInterface dialog, int id)
//            {
//                getSnapshots().getSnapshot(position).getReference().delete();
//                Toast.makeText(context, "글이 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
//                notifyDataSetChanged();
//            }
//        });
//        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener(){
//            @Override
//            public void onClick(DialogInterface dialog, int id)
//            {
//                Toast.makeText(context, "삭제가 취소되었습니다.", Toast.LENGTH_SHORT).show();
//            }
//        });
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//    }
//