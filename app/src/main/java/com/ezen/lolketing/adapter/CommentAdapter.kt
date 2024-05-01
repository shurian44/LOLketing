package com.ezen.lolketing.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.databinding.ItemCommentBinding
import com.ezen.network.model.Comment

class CommentAdapter : ListAdapter<Comment, CommentHolder>(diffUtil) {

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
            comment = currentList[position]
        )
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment) =
                oldItem.commentId == newItem.commentId

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment) =
                oldItem == newItem
        }
    }

}

class CommentHolder(
    val binding: ItemCommentBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        comment: Comment
    ) = with(binding) {
        this.comment = comment
    }

} // CommentHolder
