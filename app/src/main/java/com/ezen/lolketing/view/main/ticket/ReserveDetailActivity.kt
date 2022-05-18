package com.ezen.lolketing.view.main.ticket

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ezen.lolketing.BaseActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.SeatDialog
import com.ezen.lolketing.databinding.ActivityReserveDetailBinding
import com.ezen.lolketing.model.SeatDTO
import com.ezen.lolketing.model.Users
import com.ezen.lolketing.util.toast
import com.ezen.lolketing.view.login.LoginActivity
import com.ezen.lolketing.view.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.text.DecimalFormat

class ReserveDetailActivity : BaseActivity<ActivityReserveDetailBinding>(R.layout.activity_reserve_detail), SeatDialog.onSelectSeatListener {
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

        price = intent?.getIntExtra("price", 0) ?: 0  // ex) 9000
        binding.reservePrice.text = format.format(price)
        time = intent?.getStringExtra("time") ?: ""    // ex) 2020.02.14 17:00
        team = intent?.getStringExtra("team") ?: ""    // ex) T1:DAMWON
        binding.radio1.isChecked = true // 1명을 기본으로 설정

        // 라디오 버튼의 상태에 따라 가격 표시
        binding.radio1.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked)
                binding.reservePrice.text = format.format(price)
            else{
                binding.reservePrice.text = format.format(price * 2)
            }
        }
        // 좌석 선택 버튼 클릭
        binding.selectSeat.setOnClickListener {
            // 인원수에 맞게 다이얼로그 설정
            var seatDialog = if(binding.radio1.isChecked){
                SeatDialog(this, this, time, 1)
            } else{
                SeatDialog(this, this, time, 2)
            }
            // 다이얼로그 표시
            seatDialog.createDialog()
        }
    } // onCreate()

    override fun onStart() {
        super.onStart()
        // 유저의 캐시 조회
        firestore.collection("Users").document(auth.currentUser?.email!!).get().addOnCompleteListener {
            var user = it.result?.toObject(Users::class.java)!!
            myCache = user.cache!!
        }
    }

    // 결제하기 버튼 클릭
    fun payment(view: View) {
        // 좌석을 선택하지 않고 결제하기 버튼을 눌렀을 경우
        if(binding.reserveSelect.text == "좌석을 선택해주세요") {
            toast("좌석을 선택해주세요")
            return
        }

        pay = if(binding.radio1.isChecked) price
        else price * 2
        // 캐시가 부족한 경우
        if(myCache < pay){
//            alert(title = "잔액이 부족합니다.", message = "캐시를 충전하시겠습니까?"){
//                positiveButton("확인"){
//                    startActivity(Intent(applicationContext, CacheChargingActivity::class.java))
//                }
//                negativeButton("취소"){}
//            }.show()
            return
        }

        firestore.collection("Game").document(time).collection("Seat").document("seat").get().addOnCompleteListener {
            var seatDTO = it.result?.toObject(SeatDTO::class.java)  // 좌석 정보 가져오기
            var seats = binding.reserveSelect.text.toString().split("/")   // 선택한 좌석을 배열로 저장 (2명일 경우를 위해)
            var map = seatDTO?.seats as MutableMap  // DB에 저장된 Map 을 수정 가능한 Map 타입으로 생성
            for(seat in seats){
                // 다른 사용자가 먼저 결제했을 경우
                if(seatDTO?.seats?.get(seat) == true){
                    toast("이미 선택된 좌석입니다.")
                    return@addOnCompleteListener
                }
                map[seat] = true
            }
            // 결제 완료 된 좌석 개수 + 사용자가 결제하려는 좌석
            var count = 0
            for((key, value) in map){
                if(value){
                    count++
                }
            }
            // 모든 좌석이 선택 되었을 경우 매진으로 변경
            if(count >= 64){
                firestore.collection("Game").document(time).update("status", "매진")
            }

            seatDTO.seats = map
            // 좌석 정보 저장
            firestore.collection("Game").document(time).collection("Seat").document("seat").set(seatDTO).addOnCompleteListener {task->
                if(task.isComplete){
                    var ticket = if(binding.radio1.isChecked) 1
                                      else 2
                    // 유저의 캐시 차감 및 룰렛 기회 추가
                    firestore.collection("Users").document(auth.currentUser?.email!!).update("cache", FieldValue.increment(-pay.toDouble()), "rouletteCount", FieldValue.increment(ticket.toDouble()))
                    // 결제 완료 페이지로 이동
                    var intent = Intent(this, TicketingActivity::class.java)
                    intent.putExtra("time", time)
                    intent.putExtra("team", team)
                    intent.putExtra("seat", binding.reserveSelect.text.toString())
                    intent.putExtra("ticketCount", ticket)
                    intent.putExtra("pay", pay)
                    startActivity(intent)
                }
            }
        } // Firebase get().addOnCompleteListener()
    } // payment()

    // 선택한 좌석을 TextView 에 표시
    override fun selectSeat(seat: String) {
        binding.reserveSelect.text = seat
    }

    override fun logout(view: View) {
        auth.signOut()
        var intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    override fun moveHome(view: View) {
        var intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}