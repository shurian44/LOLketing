package com.ezen.lolketing.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ezen.lolketing.databinding.ItemTeamBinding
import com.ezen.lolketing.model.PlayerInfo

class TeamAdapter : RecyclerView.Adapter<TeamAdapter.TeamHolder>(){

    private val list = mutableListOf<PlayerInfo>()

    inner class TeamHolder(private val binding : ItemTeamBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() = with(binding) {
            val item = list[adapterPosition]
            if(item.img.isNotEmpty()){
//                Glide.with(binding.root.context).load(item.img).into(binding.playerImg)
            }
            val text = "${item.nickname}\n${item.name}\n${item.position}"
//            binding.playerName.text = text
//            binding.playerName.maxLines = 1
//
//            binding.root.setOnClickListener {
//                if(binding.playerName.maxLines == 1){
//                    binding.playerName.maxLines = 3
//                }else{
//                    binding.playerName.maxLines = 1
//                }
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamHolder =
        TeamHolder(ItemTeamBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: TeamHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = list.size

    fun addList(list: List<PlayerInfo>) {
        this.list.addAll(list)
    }

}