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
    private val dataChangeListener: () -> Unit
) : FirebaseRecyclerAdapter<ChattingDTO.Comment, ChattingAdapter.ChattingHolder>(options){

    class ChattingHolder(private val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: ChattingDTO.Comment) = with(binding) {
            txtChatting.text = model.message
            txtName.text = model.id
            if(model.team == "R"){
                txtChatting.setBackgroundResource(R.drawable.bubble_right)
                chattingLinear.gravity = Gravity.RIGHT
            }
            else{
                txtChatting.setBackgroundResource(R.drawable.bubble_left)
                chattingLinear.gravity = Gravity.LEFT
            }
        }
    }

    override fun onDataChanged() {
        super.onDataChanged()
        dataChangeListener()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChattingHolder =
        ChattingHolder(ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ChattingHolder, position: Int, model: ChattingDTO.Comment) {
        holder.bind(model)
    }
}