package com.ezen.lolketing.view.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.databinding.CellTeamBinding
import com.ezen.network.model.Team

class TeamAdapter(
    private val onClick: (Team) -> Unit
) : ListAdapter<Team, TeamViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TeamViewHolder(
            binding = CellTeamBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            onClick = onClick
        )

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Team>() {
            override fun areItemsTheSame(oldItem: Team, newItem: Team) =
                oldItem.teamId == newItem.teamId

            override fun areContentsTheSame(oldItem: Team, newItem: Team) =
                oldItem == newItem

        }
    }
}

class TeamViewHolder(
    private val binding: CellTeamBinding,
    private val onClick: (Team) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Team) {
        binding.team = item
        binding.root.setOnClickListener { onClick(item) }
    }
}