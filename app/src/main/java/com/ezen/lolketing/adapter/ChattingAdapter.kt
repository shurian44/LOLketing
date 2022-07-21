package com.ezen.lolketing.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
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

            setUILocation(txtName, txtChatting, viewChatting, model.team)
        }

        /** 선택 팀에 따라 UI 변경 **/
        private fun setUILocation(txtName: TextView, txtChat: TextView, view: View, team: String?) {
            when(team) {
                "L" -> {
                    txtName.updateLayoutParams<ConstraintLayout.LayoutParams> {
                        startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                        endToEnd = ConstraintLayout.LayoutParams.UNSET
                    }
                    txtChat.updateLayoutParams<ConstraintLayout.LayoutParams> {
                        startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                        endToEnd = ConstraintLayout.LayoutParams.UNSET
                    }
                    view.setBackgroundResource(R.drawable.bubble_left)
                }
                else -> {
                    txtName.updateLayoutParams<ConstraintLayout.LayoutParams> {
                        startToStart = ConstraintLayout.LayoutParams.UNSET
                        endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                    }
                    txtChat.updateLayoutParams<ConstraintLayout.LayoutParams> {
                        startToStart = ConstraintLayout.LayoutParams.UNSET
                        endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                    }
                    view.setBackgroundResource(R.drawable.bubble_right)
                }
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