package com.ezen.lolketing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class EventListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_list)
    }

    fun moveHome(view: View) {}
    fun logout(view: View) {}
}