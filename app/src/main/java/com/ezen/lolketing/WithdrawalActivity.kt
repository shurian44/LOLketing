package com.ezen.lolketing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_withdrawal.*
import org.jetbrains.anko.toast

class WithdrawalActivity : AppCompatActivity() {

    var auth = FirebaseAuth.getInstance()
    var firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_withdrawal)

        word_outaccount.text = "한번 회원탈퇴가 이루어지면 절대 계정을 복구할 수 없습니다.\n아래의 사항에 동의하신다면\n회원 탈퇴 요청을진행해 주시기 바랍니다."
        word_outaccount_2.text = "회원 탈퇴를 하시려면 아래의 창에 영문 대문자로 DELETE 라고 적어 주세요"

        outaccount.setOnClickListener {
            if(enterzone.text.toString() == "DELETE"){
                auth.currentUser?.delete()!!.addOnCompleteListener {
                    if(it.isComplete){
                        toast("회원 탈퇴 완료")
                        var intent = Intent(this, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                    }
                }
            }else{
                toast("입력이 잘못되었습니다.")
            }
        }

        cancle.setOnClickListener {
            finish()
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