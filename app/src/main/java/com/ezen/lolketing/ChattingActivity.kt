package com.ezen.lolketing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class ChattingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting)
    }

    fun logout(view: View) {}
}