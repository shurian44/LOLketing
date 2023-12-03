package com.ezen.lolketing.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ItemCommentBinding
import com.ezen.lolketing.model.CommentItem
import com.ezen.lolketing.view.dialog.BasicPopup
import com.ezen.lolketing.view.dialog.PopupItem

class CommentAdapter(
    private val layoutInflater: LayoutInflater,
    private val myEmail: String,
    private val onDelete: (String) -> Unit,
    private val onReport: (String, List<String>) -> Unit
) : ListAdapter<CommentItem, CommentHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder =
        CommentHolder(
            ItemCommentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: CommentHolder, position: Int) {
        holder.bind(
            comment = currentList[position],
            layoutInflater = layoutInflater,
            myEmail = myEmail,
            onDelete = onDelete,
            onReport = onReport
        )

        if (position % 2 == 0) {
            holder.binding.root.setBackgroundResource(R.color.light_black)
        } else {
            holder.binding.root.setBackgroundResource(R.color.black)
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CommentItem>() {
            override fun areItemsTheSame(oldItem: CommentItem, newItem: CommentItem) =
                oldItem.documentId == newItem.documentId

            override fun areContentsTheSame(oldItem: CommentItem, newItem: CommentItem) =
                oldItem == newItem
        }
    }

}

class CommentHolder(
    val binding: ItemCommentBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        comment: CommentItem,
        layoutInflater: LayoutInflater,
        myEmail: String,
        onDelete: (String) -> Unit,
        onReport: (String, List<String>) -> Unit
    ) = with(binding) {
        this.comment = comment

        viewMenu.setOnClickListener { view ->
            showPopup(
                view = view,
                comment = comment,
                layoutInflater = layoutInflater,
                myEmail = myEmail,
                onDelete = onDelete,
                onReport = onReport
            )
        }
    }

    private fun showPopup(
        view: View,
        comment: CommentItem,
        layoutInflater: LayoutInflater,
        myEmail: String,
        onDelete: (String) -> Unit,
        onReport: (String, List<String>) -> Unit
    ) {
        val commentEmail = comment.email
        val documentId = comment.documentId

        if (myEmail == commentEmail) {
            myCommentPopup(
                view,
                layoutInflater,
                documentId,
                onDelete
            )
        } else {
            otherCommentPopup(
                view,
                layoutInflater,
                documentId,
                comment.reportList,
                onReport
            )
        }
    }

    private fun myCommentPopup(
        view: View,
        layoutInflater: LayoutInflater,
        documentId: String,
        onDelete: (String) -> Unit
    ) {
        BasicPopup(
            view = view,
            layoutInflater = layoutInflater,
            list = listOf(
                PopupItem(
                    text = view.context.getString(R.string.select_delete),
                    onClick = { onDelete(documentId) }
                ),
            )
        ).showPopup()
    }

    private fun otherCommentPopup(
        view: View,
        layoutInflater: LayoutInflater,
        documentId: String,
        reportList: List<String>,
        onReport: (String, List<String>) -> Unit
    ) {
        BasicPopup(
            view = view,
            layoutInflater = layoutInflater,
            list = listOf(
                PopupItem(
                    text = view.context.getString(R.string.select_report),
                    onClick = { onReport(documentId, reportList) }
                ),
            )
        ).showPopup()
    }

} // CommentHolder
