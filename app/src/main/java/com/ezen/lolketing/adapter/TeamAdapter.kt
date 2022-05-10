package com.ezen.lolketing.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ezen.lolketing.databinding.ItemTeamBinding
import com.ezen.lolketing.model.TeamDTO
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class TeamAdapter(options : FirestoreRecyclerOptions<TeamDTO.PlayerDTO>) :
    FirestoreRecyclerAdapter<TeamDTO.PlayerDTO, TeamAdapter.TeamHolder>(options){

    class TeamHolder(private val binding : ItemTeamBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: TeamDTO.PlayerDTO) = with(binding) {
            if(model.img != ""){
                Glide.with(binding.root.context).load(model.img).into(binding.playerImg)
            }
            var text = "${model.nickname}\n${model.name}\n${model.position}"
            binding.playerName.text = text
            binding.playerName.maxLines = 1

            binding.root.setOnClickListener {
                if(binding.playerName.maxLines == 1){
                    binding.playerName.maxLines = 3
                }else{
                    binding.playerName.maxLines = 1
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamHolder =
        TeamHolder(ItemTeamBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: TeamHolder, position: Int, model: TeamDTO.PlayerDTO) {
        holder.bind(model)
    }

}