package com.ezen.lolketing.view.main.board.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.databinding.ItemBoardBinding
import com.ezen.lolketing.model.Board

class BoardListAdapter(
    val onclickListener : (Board) -> Unit,
    val onLongClickListener: (Board, View) -> Unit,
) : ListAdapter<Board, BoardListAdapter.ViewHolder>(diffUtil){

    inner class ViewHolder(val binding : ItemBoardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(board: Board) = with(binding) {
            this.board = board
            root.setOnClickListener {
                onclickListener(board)
            }

            root.setOnLongClickListener {
                onLongClickListener(board, it)
                true
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Board>() {
            override fun areItemsTheSame(oldItem: Board, newItem: Board): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Board, newItem: Board): Boolean =
                oldItem.userId == newItem.userId && oldItem.timestamp == newItem.timestamp

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