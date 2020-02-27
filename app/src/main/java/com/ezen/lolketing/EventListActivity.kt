package com.ezen.lolketing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_event_list.*

class EventListActivity : AppCompatActivity() {
    private var auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_list)

        event_card1.setOnClickListener {
            var intent = Intent(this, EventDetailActivity::class.java)
            intent.putExtra("page", 1)
            startActivity(intent)
        }
        event_card2.setOnClickListener{
            var intent = Intent(this, EventDetailActivity::class.java)
            intent.putExtra("page", 2)
            startActivity(intent)
        }
        event_card3.setOnClickListener{
            startActivity(Intent(this, RouletteActivity::class.java))
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