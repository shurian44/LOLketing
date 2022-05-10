package com.ezen.lolketing.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ItemChatBinding
import com.ezen.lolketing.model.ChattingDTO
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class ChattingAdapter(
    options : FirebaseRecyclerOptions<ChattingDTO.Comment>,
    private val listener : moveScrollListener
) : FirebaseRecyclerAdapter<ChattingDTO.Comment, ChattingAdapter.ChattingHolder>(options){

    class ChattingHolder(private val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: ChattingDTO.Comment) {
            binding.txtChatting.text = model.message
            binding.txtName.text = model.id
            if(model.team == "R"){
                binding.txtChatting.setBackgroundResource(R.drawable.bubble_right)
                binding.chattingLinear.gravity = Gravity.RIGHT
            }
            else{
                binding.txtChatting.setBackgroundResource(R.drawable.bubble_left)
                binding.chattingLinear.gravity = Gravity.LEFT
            }
        }
    }

    override fun onDataChanged() {
        super.onDataChanged()
        listener.moveScroll()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChattingHolder =
        ChattingHolder(ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ChattingHolder, position: Int, model: ChattingDTO.Comment) {
        holder.bind(model)
    }

    interface moveScrollListener{
        fun moveScroll()
    }
}