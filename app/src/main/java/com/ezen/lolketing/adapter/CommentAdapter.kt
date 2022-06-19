package com.ezen.lolketing.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ItemCommentBinding
import com.ezen.lolketing.model.Board

class CommentAdapter(
//    private val deleteListener : (Board.Comment) -> Unit
) : RecyclerView.Adapter<CommentAdapter.CommentHolder>() { // class CommentAdapter

    private val commentList = mutableListOf<Board.Comment >()

    inner class CommentHolder(private val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: Board.Comment) = with(binding) {
            this.comment = comment

            if (adapterPosition % 2 == 0) {
                root.setBackgroundResource(R.color.light_black)
            } else {
                root.setBackgroundResource(R.color.black)
            }
//            binding.imageViewDelete.setOnClickListener {
//                deleteListener(comment)
//            }
        }
    } // CommentHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder =
        CommentHolder(ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: CommentHolder, position: Int) {
        holder.bind(commentList[position])
    }

    fun commentDelete(context: Context?, position: Int, email: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("댓글 삭제").setMessage("정말 삭제하시겠습니까?")
        builder.setPositiveButton("네") { dialog, id ->

//            if (email == auth.currentUser!!.email) {
//                snapshots.getSnapshot(position).reference.delete()
//                Toast.makeText(context, "댓글이 삭제 되었습니다.", Toast.LENGTH_SHORT).show()
//                FirebaseFirestore.getInstance().collection("Board").document(documentID)
//                    .update("commentCounts", FieldValue.increment(-1))
//            } else {
//                Toast.makeText(context, "삭제 권한이 없습니다", Toast.LENGTH_SHORT).show()
//            }
            //                notifyDataSetChanged();
        }
        builder.setNegativeButton("아니오") { dialog, id ->
            Toast.makeText(
                context,
                "삭제 취소.",
                Toast.LENGTH_SHORT
            ).show()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    override fun getItemCount(): Int =
        commentList.size

    fun addList(list: List<Board.Comment>) {
        commentList.addAll(list)
    }

}
