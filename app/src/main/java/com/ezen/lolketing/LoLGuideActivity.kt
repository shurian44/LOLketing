package com.ezen.lolketing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth

class LoLGuideActivity : AppCompatActivity() {
    private var auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lol_guide)
    }

    // 선택에 맞게 인탠트로 값을 넘겨 상세 페이지로 이동
    fun moveGuide(view: View) {
        var intent = Intent(this, LoLGuideDetailActivity::class.java)
        when(view.id){
            R.id.card1-> intent.putExtra("status", "aos")
            R.id.card2-> intent.putExtra("status", "rule")
            R.id.card3-> intent.putExtra("status", "position")
            R.id.card4-> intent.putExtra("status", "nature")
            R.id.card5-> intent.putExtra("status", "score")
            R.id.card6-> intent.putExtra("status", "terms")
        }
        startActivity(intent)
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