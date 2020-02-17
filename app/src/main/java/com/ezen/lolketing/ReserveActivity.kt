package com.ezen.lolketing

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_reserve.*

class ReserveActivity : AppCompatActivity() {

    lateinit var time : String
    lateinit var team : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserve)

        time = intent.getStringExtra("time")
        team = intent.getStringExtra("team")
        // 2020.02.14 17:00
        reserve_time.text = time
        // T1:DAMWON
        reserve_match.text = team

        reserve_button.setOnClickListener {
            var intent = Intent(this, ReserveDetailActivity::class.java)
            intent.putExtra("time", time)
            intent.putExtra("team", team)
            startActivity(intent)
        }
    }

    fun logout(view: View?) {}
}