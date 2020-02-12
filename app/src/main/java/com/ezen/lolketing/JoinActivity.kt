package com.ezen.lolketing

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ezen.lolketing.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_join.*
import java.util.regex.Pattern


class JoinActivity : AppCompatActivity() {
    private var auth = FirebaseAuth.getInstance()
    private var firestore = FirebaseFirestore.getInstance()
    var checkId = false
    var checkPw = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        textChanged()

        btn_join.setOnClickListener {
            var email = join_id.text?.toString() ?: ""
            var pw = join_pw.text?.toString() ?: ""
            var pw_ck = join_pw_ck.text?.toString() ?: ""
            if(!checkId || !checkPw){
                Toast.makeText(this, "입력한 내용을 확인해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(pw == pw_ck){
                auth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener {
                    if(it.isSuccessful){
                        var user = Users()
                        user.id = email
                        Log.e("test", "화원가입 ${user.id}")
                        firestore.collection("Users").document(email).set(user).addOnCompleteListener {
                            if(it.isComplete){
                                startActivity(Intent(this, LoginActivity::class.java))
                                auth.signOut()
                                finish()
                            }
                        }
                    }else{
                        Toast.makeText(this, "가입 오류 : ${it.exception}", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this, "비밀번호를 확인해 주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun textChanged(){
        join_id.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                val id = join_id.text.toString()
                Log.e("test", "값 : ${android.util.Patterns.EMAIL_ADDRESS.matcher(id).matches()}")
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(id).matches()){
                    join_id.error = "아이디 조건이 일치하지 않습니다."
                    checkId = false
                }
                else checkId = true
            }
            override fun afterTextChanged(editable: Editable) {}
        })

        join_pw.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                val pattern = "\\w+"
                val pw: String = join_pw.text.toString()
                if (Pattern.matches(pattern, pw)) join_pw.error = "비밀번호 조건이 일치하지 않습니다.\n영문 또는 숫자와 특수문자 필수\n"
                if(pw.length !in 6..20) join_pw.error = "비밀번호는 6~20로 생성해주세요"
            }
            override fun afterTextChanged(editable: Editable) {}
        })

        join_pw_ck.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if(join_pw.text.toString() != join_pw_ck.text.toString()){
                    join_pw_ck.error = "비밀번호가 일치하지 않습니다."
                    checkPw = false
                }
                else checkPw = true
            }
            override fun afterTextChanged(editable: Editable) {}
        })
    }
}