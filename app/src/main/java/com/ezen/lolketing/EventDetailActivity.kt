package com.ezen.lolketing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import kotlinx.android.synthetic.main.activity_event_detail.*

class EventDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)

        var status = intent.getIntExtra("status", 0)

        when(status){
            0->{
                // 신규회원
                event_txt1.text = Html.fromHtml("<span>신규 회원가입 하시는 고객분께는</span><br><span><font color=\"#6200EE\">500P</font>를 드립니다!</span>")
            }
            1->{
                // 룰렛
            }
        }
    }

    fun moveHome(view: View) {}
    fun logout(view: View) {}
}