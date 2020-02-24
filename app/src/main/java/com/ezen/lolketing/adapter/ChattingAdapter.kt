package com.ezen.lolketing.adapter

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.R
import com.ezen.lolketing.model.ChattingDTO
import com.ezen.lolketing.model.Users
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_chat.view.*
import java.text.SimpleDateFormat

class ChattingAdapter(options : FirebaseRecyclerOptions<ChattingDTO.Comment>, listener : moveScrollListener) :
        FirebaseRecyclerAdapter<ChattingDTO.Comment, ChattingAdapter.ChattingHolder>(options){

//    var format = SimpleDateFormat("HH:mm")
    var listener = listener

    class ChattingHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onDataChanged() {
        super.onDataChanged()
        listener.moveScroll()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChattingHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ChattingHolder(view)
    }

    override fun onBindViewHolder(holder: ChattingHolder, position: Int, model: ChattingDTO.Comment) {
        var item = holder.itemView
        item.txt_chatting.text = model.message
        item.txt_name.text = model.id
        if(model.team == "R"){
            item.txt_chatting.setBackgroundResource(R.drawable.bubble_right)
            item.chatting_linear.gravity = Gravity.RIGHT
        }
        else{
            item.txt_chatting.setBackgroundResource(R.drawable.bubble_left)
            item.chatting_linear.gravity = Gravity.LEFT
        }
    }

    interface moveScrollListener{
        fun moveScroll()
    }
}