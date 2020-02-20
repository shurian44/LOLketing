package com.ezen.lolketing

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ezen.lolketing.model.SeatDTO
import com.ezen.lolketing.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_reserve_detail.*
import org.jetbrains.anko.toast
import java.text.DecimalFormat

class ReserveDetailActivity : AppCompatActivity(), SeatDialog.onSelectSeatListener {
    private var firestore = FirebaseFirestore.getInstance()
    private var auth = FirebaseAuth.getInstance()
    private var myCache = 0
    lateinit var time : String
    lateinit var team : String
    var price = 0
    var format = DecimalFormat("###,###,###,###")
    var pay = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserve_detail)

        price = intent.getIntExtra("price", 0)
        reserve_price.text = format.format(price)

        time = intent.getStringExtra("time")
        team = intent.getStringExtra("team")
        radio1.isChecked = true

        radio1.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked)
                reserve_price.text = format.format(price)
            else{
                reserve_price.text = format.format(price * 2)
            }
        }

        select_seat.setOnClickListener {
            var seatDialog = if(radio1.isChecked){
                SeatDialog(this, this, time, 1)
            } else{
                SeatDialog(this, this, time, 2)
            }
            seatDialog.createDialog()
        }

        firestore.collection("Users").document(auth.currentUser?.email!!).get().addOnCompleteListener {
            var user = it.result?.toObject(Users::class.java)!!
            myCache = user.cache!!
        }

        reserve_button.setOnClickListener {
            if(reserve_select.text == "좌석을 선택해주세요") {
                toast("좌석을 선택해주세요")
                return@setOnClickListener
            }

            pay = if(radio1.isChecked) price
                           else price * 2
            if(myCache < pay){
                toast("잔액이 부족합니다.\n캐시를 충전해주세요")
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
                var count = 0
                for((key, value) in map){
                    if(value){
                        count++
                    }
                }
                if(count >= 10){
                    firestore.collection("Game").document(time).update("status", "매진")
                }

                seatDTO.seats = map
                firestore.collection("Game").document(time).collection("Seat").document("seat").set(seatDTO).addOnCompleteListener {task->
                    if(task.isComplete){
                        firestore.collection("Users").document(auth.currentUser?.email!!).update("cache", FieldValue.increment(-pay.toDouble()))
                        var ticket = if(radio1.isChecked) 1
                                         else 2
                        var intent = Intent(this, TicketingActivity::class.java)
                        //2020.02.15_T1:DAMWON_A1a
                        intent.putExtra("time", time)
                        intent.putExtra("team", team)
                        intent.putExtra("seat", reserve_select.text.toString())
                        intent.putExtra("ticketCount", ticket)
                        intent.putExtra("pay", pay)
                        toast("좌석 선택 성공")
                        startActivity(intent)
                        finish()
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