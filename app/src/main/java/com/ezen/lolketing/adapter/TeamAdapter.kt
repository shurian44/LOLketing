package com.ezen.lolketing.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.R
import com.ezen.lolketing.model.TeamDTO
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.item_team.view.*

class TeamAdapter(options : FirestoreRecyclerOptions<TeamDTO.PlayerDTO>) :
    FirestoreRecyclerAdapter<TeamDTO.PlayerDTO, TeamAdapter.TeamHolder>(options){

    class TeamHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_team, parent, false)

        return TeamHolder(view)
    }

    override fun onBindViewHolder(holder: TeamHolder, position: Int, model: TeamDTO.PlayerDTO) {
        val item = holder.itemView

        var text = "${model.nickname}\n${model.name}\n${model.position}"
        item.player_name.text = text
        item.player_name.maxLines = 1

        item.setOnClickListener {
            if(item.player_name.maxLines == 1){
                item.player_name.maxLines = 3
            }else{
                item.player_name.maxLines = 1
            }
        }
    }

}