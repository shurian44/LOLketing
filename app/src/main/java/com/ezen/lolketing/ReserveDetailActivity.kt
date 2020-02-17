package com.ezen.lolketing

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ezen.lolketing.model.SeatDTO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_reserve_detail.*
import org.jetbrains.anko.toast

class ReserveDetailActivity : AppCompatActivity(), SeatDialog.onSelectSeatListener {
    lateinit var time : String
    var firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserve_detail)

        time = intent.getStringExtra("time")
        radio1.isChecked = true
        radio1.setOnCheckedChangeListener { buttonView, isChecked ->  }
        select_seat.setOnClickListener {
            var seatDialog = if(radio1.isChecked){
                SeatDialog(this, this, time, 1)
            } else{
                SeatDialog(this, this, time, 2)
            }
            seatDialog.createDialog()
        }
        reserve_button.setOnClickListener {
            if(reserve_select.text == "좌석을 선택해주세요") {
                toast("좌석을 선택해주세요")
                return@setOnClickListener
            }

            firestore.collection("Game").document(time).collection("Seat").document("seat").get().addOnCompleteListener {
                var seatDTO = it.result?.toObject(SeatDTO::class.java)
                var seats = reserve_select.text.toString().split("/")
                var map = seatDTO?.seats as MutableMap
                for(seat in seats){
                    if(seatDTO?.seats?.get(seat) == true){
                        toast("이미 선택된 좌석입니다.")
                        return@addOnCompleteListener
                    }
                    map[seat] = true
                }
                toast("좌석 선택 성공")
                seatDTO.seats = map
                firestore.collection("Game").document(time).collection("Seat").document("seat").set(seatDTO).addOnCompleteListener {task->
                    if(task.isComplete){
                        var intent = Intent(this, TicketingActivity::class.java)
                        //2020.02.15_T1:DAMWON_A1a
                        intent.putExtra("code", "${time}_${intent.getStringExtra("team")}_${reserve_select.text}")
                        startActivity(intent)
                    }
                }
            }
        }
    }

    fun logout(view: View?) {}
    override fun selectSeat(seat: String) {
        reserve_select.text = seat
    }
}