package com.ezen.lolketing.view.main.guide

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ezen.lolketing.BaseActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityLolGuideBinding
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.util.startActivity
import com.ezen.lolketing.view.login.LoginActivity
import com.ezen.lolketing.view.main.MainActivity
import com.ezen.lolketing.view.main.manager.ManagerActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoLGuideActivity : BaseActivity<ActivityLolGuideBinding>(R.layout.activity_lol_guide) {
    @Inject lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.activity = this

    }

    // 선택에 맞게 인탠트로 값을 넘겨 상세 페이지로 이동
    fun moveGuide(view: View) {
        val intent = createIntent(LoLGuideDetailActivity::class.java)
        when(view.id){
            R.id.card1 -> intent.putExtra("status", "aos")
            R.id.card2 -> intent.putExtra("status", "rule")
            R.id.card3 -> intent.putExtra("status", "position")
            R.id.card4 -> intent.putExtra("status", "nature")
            R.id.card5 -> intent.putExtra("status", "score")
            R.id.card6 -> intent.putExtra("status", "terms")
        }
        startActivity(intent)
    }

    /** 로그아웃 버튼 클릭 **/
    override fun logout(view: View) {
        auth.signOut()
        startActivity(LoginActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TOP)
        finish()
    }

    /** 관리자 페이지 버튼 클릭 **/
    fun moveHome(view: View) {
        startActivity(MainActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TOP)
        finish()
    }
}