package com.ezen.lolketing.adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ItemReserveTicketBinding
import com.ezen.lolketing.model.GameDTO
import com.ezen.lolketing.view.main.ticket.ReserveActivity
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class ReserveAdapter (
    options : FirestoreRecyclerOptions<GameDTO>,
    private val listener : reserveItemClickListener
) : FirestoreRecyclerAdapter<GameDTO, ReserveAdapter.ReserveHolder>(options){

    inner class ReserveHolder(private val binding: ItemReserveTicketBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: GameDTO) {
            // item 세팅
            var teams = model.team?.split(":")
            setImage(binding.leftTeam, teams!![0])
            setImage(binding.rightTeam, teams!![1])
            binding.reserveText.text = "${model.date}\n${model.time}"

            binding.root.setOnClickListener {
                // 매진, 종료, 오픈 예정일 때에는 토스트 메시지만 보여주기
                when(binding.reserveStatus.text){
                    "매진"->{
                        Toast.makeText(binding.root.context, "매진되었습니다.", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    "종료"->{
                        Toast.makeText(binding.root.context, "종료되었습니다.", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    "오픈예정"->{
                        Toast.makeText(binding.root.context, "오픈 예정입니다.", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                }
                // 예약 상태이면 예약페이지로 이동
                var intent = Intent(binding.root.context, ReserveActivity::class.java)
                intent.putExtra("time", "${model.date} ${model.time}")
                intent.putExtra("team", model.team)
                listener.reserveSelect(intent)
            }

            // 아이템 생테 설정
            setState(binding, model)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReserveHolder =
        ReserveHolder(ItemReserveTicketBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ReserveHolder, position: Int, model: GameDTO) {
        holder.bind(model)
    }

    // 팀에 맞는 이미지 세팅
    private fun setImage(image : ImageView, team : String? = ""){
        when(team){
            "T1"-> image.setImageResource(R.drawable.logo_t1)
            "Griffin"-> image.setImageResource(R.drawable.logo_damwon)
            "DAMWON Gaming"-> image.setImageResource(R.drawable.icon_damwon)
            "SANDBOX Gaming"-> image.setImageResource(R.drawable.icon_sandbox)
            "Afreeca Freecs"-> image.setImageResource(R.drawable.icon_afreeca)
            "Gen.G Esports"-> image.setImageResource(R.drawable.icon_geng)
            "DragonX"-> image.setImageResource(R.drawable.icon_dragonx)
            "KT Rolster"-> image.setImageResource(R.drawable.icon_rolster)
            "APK PRINCE"-> image.setImageResource(R.drawable.icon_apk_prince)
            "Hanwha Life Esports"-> image.setImageResource(R.drawable.icon_hanwha)
        }
    }

    //아이템 상태 설정
    private fun setState(binding: ItemReserveTicketBinding, model: GameDTO){
        // 예매 상태 : 예매, 매진, 종료, 오픈 예정
        // 매진 : 좌석이 다 팔린 상태
        // 종료 : 게임 시작 4시간 전 이후
        // 오픈 예정 : 게임 6일 전
        // 예매 : 게임 5일전 2시 ~ 게임 시작 4시간 이전
        var dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm")
        var date = dateFormat.parse("${model.date} ${model.time}")
        // 종료 상태 계산 후 데이터베이스에 저장
        date.hours = date.hours - 4
        var mDate = Date()
        if(model.status == "예매" && mDate > date){
            FirebaseFirestore.getInstance().collection("Game").document("${model.date} ${model.time}").update("status", "종료")
        }
        // 오픈 예전, 예매 계산 후 데이터베이스에 저장
        date.date = date.date - 5
        date.hours = 2
        if(date.after(mDate)){
            FirebaseFirestore.getInstance().collection("Game").document("${model.date} ${model.time}").update("status", "오픈예정")
        }else if(model.status == "오픈예정" && date.before(mDate)){
            FirebaseFirestore.getInstance().collection("Game").document("${model.date} ${model.time}").update("status", "예매")
        }
        // 상태에 따라 UI 표시
        when(model.status){
            "예매"->{
                binding.reserveView.setBackgroundColor(Color.parseColor("#11CC88"))
                binding.reserveStatus.text = "예매"
            }
            "매진"->{
                binding.reserveView.setBackgroundColor(Color.parseColor("#FF0000"))
                binding.reserveStatus.text = "매진"
            }
            "종료"->{
                binding.reserveView.setBackgroundColor(Color.parseColor("#555555"))
                binding.reserveStatus.text = "종료"
            }
            "오픈예정"->{
                binding.reserveView.setBackgroundColor(Color.parseColor("#555555"))
                binding.reserveStatus.text = "오픈\n예정"
            }
        }
    }

    // 아이템 클릭 리스너
    interface reserveItemClickListener{
        fun reserveSelect(intent : Intent)
    }

}