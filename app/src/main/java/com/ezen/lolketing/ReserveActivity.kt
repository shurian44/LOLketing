package com.ezen.lolketing

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_reserve.*
import java.text.SimpleDateFormat
import java.util.*

class ReserveActivity : AppCompatActivity() {

    lateinit var time : String
    lateinit var team : String
    var price : Int = 0
    private var auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserve)

        time = intent.getStringExtra("time")
        team = intent.getStringExtra("team")
        // 2020.02.14 17:00
        reserve_time.text = time
        // T1:DAMWON
        reserve_match.text = team

        // 요일 구하기
        var dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm")
        var date = dateFormat.parse(time)
        var calendar = Calendar.getInstance()
        calendar.time = date
        // 1 = 일, 2 = 월 ... 7 = 토
        price = when(calendar.get(Calendar.DAY_OF_WEEK)){
            1, 7 -> 11000
            else -> 9000
        }

        reserve_button.setOnClickListener {
            var intent = Intent(this, ReserveDetailActivity::class.java)
            intent.putExtra("time", time)
            intent.putExtra("team", team)
            intent.putExtra("price", price)
            startActivity(intent)
        }
    }

    fun logout(view: View) {
        auth.signOut()
        var intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    fun moveHome(view: View) {
        var intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}