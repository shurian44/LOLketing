package com.ezen.lolketing.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ItemCommentBinding
import com.ezen.lolketing.model.Board
import com.ezen.lolketing.view.dialog.BoardMenuPopup

class CommentAdapter(
    private val layoutInflater: LayoutInflater,
    private val myEmail : String
) : RecyclerView.Adapter<CommentAdapter.CommentHolder>() { // class CommentAdapter

    private val commentList = mutableListOf<Board.Comment >()
    private var deleteLister : ((String) -> Unit)? = null
    private var reportLister : ((String, List<String>?) -> Unit)? = null

    inner class CommentHolder(private val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: Board.Comment) = with(binding) {
            this.comment = comment

            if (adapterPosition % 2 == 0) {
                root.setBackgroundResource(R.color.light_black)
            } else {
                root.setBackgroundResource(R.color.black)
            }

            txtReport.isVisible = (comment.reportList?.size ?: 0) > 5

            viewMenu.setOnClickListener { view ->
                createMenu(view, comment)
            }
        }
    } // CommentHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder =
        CommentHolder(ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: CommentHolder, position: Int) {
        holder.bind(commentList[position])
    }

    private fun createMenu(view: View, comment: Board.Comment) {
        val commentEmail = comment.email ?: return
        val documentId = comment.documentId ?: return

        BoardMenuPopup(layoutInflater).also {
            if (myEmail == commentEmail) {
                it.createPopup(BoardMenuPopup.MY_COMMENT)
            } else {
                it.createPopup(BoardMenuPopup.ANOTHER_PERSON_COMMENT)
            }

            it.setListener(
                deleteListener = {
                    deleteLister?.invoke(documentId)
                },
                reportListener = {
                    reportLister?.invoke(documentId, comment.reportList)
                }
            )
            it.showPopup(view)
        }
    }

    fun setDeleteListener(listener : (String) -> Unit) {
        deleteLister = listener
    }

    fun setReportListener(listener: (String, List<String>?) -> Unit) {
        reportLister = listener
    }

    override fun getItemCount(): Int =
        commentList.size

    fun addList(list: List<Board.Comment>) {
        commentList.addAll(list)
    }

}
