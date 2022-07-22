package com.ezen.lolketing.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ItemReserveTicketBinding
import com.ezen.lolketing.model.Ticket
import com.ezen.lolketing.util.Code
import com.ezen.lolketing.util.setTeamLogoImageView

class ReserveAdapter(
    val onclickListener: (Ticket) -> Unit
) : ListAdapter<Ticket, ReserveAdapter.ReserveHolder>(diffUtil) {

    inner class ReserveHolder(private val binding: ItemReserveTicketBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ticket: Ticket) = with(binding) {

            // item 세팅
            val teams = ticket.team.split(":")
            if (teams.isEmpty() || teams.size < 2) {
                currentList.removeAt(adapterPosition)
                return@with
            }

            setTeamLogoImageView(imgTeam1, teams[0])
            setTeamLogoImageView(imgTeam2, teams[1])
            txtTicketingTime.text = txtTicketingTime.context.getString(R.string.ticketing_time, ticket.date, ticket.time)

            // 상태에 따라 UI 표시
            when(ticket.status){
                Code.TICKETING_ON.code->{
                    txtTicketingStatus.setBackgroundResource(R.color.sub_color)
                    txtTicketingStatus.text = Code.TICKETING_ON.codeName
                }
                Code.TICKETING_SOLD_OUT.code->{
                    txtTicketingStatus.setBackgroundResource(R.color.orange)
                    txtTicketingStatus.text = Code.TICKETING_SOLD_OUT.codeName
                }
                Code.TICKETING_FINISH.code->{
                    txtTicketingStatus.setBackgroundResource(R.color.light_gray)
                    txtTicketingStatus.text = Code.TICKETING_FINISH.codeName
                }
                Code.TICKETING_SCHEDULE_OPEN.code->{
                    txtTicketingStatus.setBackgroundResource(R.color.yellow)
                    txtTicketingStatus.text = Code.TICKETING_SCHEDULE_OPEN.codeName
                }
            }

            // 배경색 포지션 값에 따라 변경
            if (adapterPosition % 2 == 0) {
                root.setBackgroundResource(R.color.black)
            } else {
                root.setBackgroundResource(R.color.light_black)
            }

            root.setOnClickListener {
                onclickListener(ticket)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReserveHolder =
        ReserveHolder(ItemReserveTicketBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(holder: ReserveHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Ticket>() {
            override fun areItemsTheSame(oldItem: Ticket, newItem: Ticket): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Ticket, newItem: Ticket): Boolean =
                oldItem.date == newItem.date && oldItem.time == oldItem.time
        }
    }


}