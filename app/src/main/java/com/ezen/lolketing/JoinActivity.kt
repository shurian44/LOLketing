package com.ezen.lolketing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ezen.lolketing.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_join.*

class JoinActivity : AppCompatActivity() {
    private var auth = FirebaseAuth.getInstance()
    private var firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        btn_join.setOnClickListener {
            var email = join_id.text?.toString() ?: ""
            var pw = join_pw.text?.toString() ?: ""
            var pw_ck = join_pw_ck.text?.toString() ?: ""
            if(email == "" || pw == "" || pw_ck == ""){
                Toast.makeText(this, "값을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(pw == pw_ck){
                auth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener {
                    if(it.isSuccessful){
                        var user = Users()
                        user.id = email
                        firestore.collection("Users").document(email).set(user)
                        startActivity(Intent(this, LoginActivity::class.java))
                        auth.signOut()
                        finish()
                    }else{
                        Toast.makeText(this, "가입 오류 : ${it.exception}", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this, "비밀번호를 확인해 주세요", Toast.LENGTH_SHORT).show()
            }
        }

    }
}