package com.ezen.lolketing.adapter

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.R
import com.ezen.lolketing.ReserveActivity
import com.ezen.lolketing.model.GameDTO
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.item_reserve_ticket.view.*

class ReserveAdapter (options : FirestoreRecyclerOptions<GameDTO>, listener : reserveOnClick)
    : FirestoreRecyclerAdapter<GameDTO, ReserveAdapter.ReserveHolder>(options){

    var listener = listener

    class ReserveHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReserveHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_reserve_ticket, parent, false)
        return ReserveHolder(view)
    }

    override fun onBindViewHolder(holder: ReserveHolder, position: Int, model: GameDTO) {
        val item = holder.itemView
        var teams = model.team?.split(":")
        Log.e("test", "결과 : $teams")
        setImage(item.left_team, teams!![0])
        setImage(item.right_team, teams!![1])
        when(model.status){
            "예약"->{
                item.reserve_view.setBackgroundColor(Color.parseColor("#11CC88"))
                item.reserve_status.text = "예약"
            }
            "매진"->{
                item.reserve_view.setBackgroundColor(Color.parseColor("#FF0000"))
                item.reserve_status.text = "매진"
            }
            "종료"->{
                item.reserve_view.setBackgroundColor(Color.parseColor("#555555"))
                item.reserve_status.text = "종료"
            }
            "오픈예정"->{
                item.reserve_view.setBackgroundColor(Color.parseColor("#555555"))
                item.reserve_status.text = "오픈\n예정"
            }
        }
        item.reserve_text.text = "${model.date}\n${model.time}"

        item.setOnClickListener {
            when(item.reserve_status.text){
                "매진"->{
                    Toast.makeText(item.context, "매진되었습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                "종료"->{
                    Toast.makeText(item.context, "종료되었습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                "오픈예정"->{
                    Toast.makeText(item.context, "오픈 예정입니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            var intent = Intent(item.context, ReserveActivity::class.java)
            intent.putExtra("time", "${model.date} ${model.time}")
            intent.putExtra("team", model.team)
            listener.reserveClick(intent)
        }
    }

    fun setImage(image : ImageView, team : String? = ""){
        when(team){
            "T1"-> image.setImageResource(R.drawable.logo_t1)
            "GRIFFIN"-> image.setImageResource(R.drawable.icon_griffin)
            "DAMWON Gamming"-> image.setImageResource(R.drawable.icon_damwon)
            "SANDBOX Gamming"-> image.setImageResource(R.drawable.icon_sandbox)
            "Afreeca Freecs"-> image.setImageResource(R.drawable.icon_afreeca)
            "Gen.G Esports"-> image.setImageResource(R.drawable.icon_geng)
            "DragonX"-> image.setImageResource(R.drawable.icon_dragonx)
            "kt Rolster"-> image.setImageResource(R.drawable.icon_rolster)
            "APK Prince"-> image.setImageResource(R.drawable.icon_apk_prince)
            "Hanwha Life Esports"-> image.setImageResource(R.drawable.icon_hanwha)
        }
    }

    interface reserveOnClick{
        fun reserveClick(intent : Intent)
    }

}