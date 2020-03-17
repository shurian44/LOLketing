package com.ezen.lolketing

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ezen.lolketing.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_join.*
import org.jetbrains.anko.toast
import java.util.regex.Pattern


class JoinActivity : AppCompatActivity() {
    private var auth = FirebaseAuth.getInstance()
    private var firestore = FirebaseFirestore.getInstance()
    var checkId = false
    var checkPw = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        editTextConstraints() // EditText 제한조건
    }

    fun emailJoin(view: View) {
        var email = join_id.text.toString()
        var pw = join_pw.text.toString()
        var pw_ck = join_pw_ck.text.toString()

        // 내용을 입력하지 않았을 경우
        if(email.isEmpty() || pw.isEmpty() || pw_ck.isEmpty()){
            toast("값을 입력해주세요")
            return
        }

        // 입력 양식에 맞지 않았을 경우
        if(!checkId || !checkPw){
            toast("입력한 내용을 확인해주세요")
            return
        }

        // 비밀번호와 비밀번호 채크가 같을 경우
        if(pw == pw_ck){
            // 이메일 회원가입
            auth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener {
                if(it.isSuccessful){ // 성공
                    var user = Users()
                    user.id = email
                    // cloud firestore에 유저 등록
                    firestore.collection("Users").document(email).set(user).addOnCompleteListener {
                        if(it.isComplete){
                            startActivity(Intent(this, LoginActivity::class.java))
                            auth.signOut()
                            finish()
                        }
                    }
                }else{ // 실패
                    toast("가입 오류 : ${it.exception}")
                }
            }
        }
        else{ // 비밀번호와 비밀번호 채크가 일치하지 않을 경우
            toast("비밀번호를 확인해 주세요")
            return
        }
    }

    // EditText 제한조건
    private fun editTextConstraints(){
        join_id.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                val id = join_id.text.toString()
                // 이메일 형식인지 체크
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(id).matches()){
                    join_id.error = "이메일 형식의 아이디를 입력해주세요."
                    checkId = false
                }
                else
                    checkId = true
            }
            override fun afterTextChanged(editable: Editable) {}
        })

        join_pw.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                val pattern = "\\w+"
                val pw: String = join_pw.text.toString()
                // 특수문자가 들어가는지 체크
                if (Pattern.matches(pattern, pw))
                    join_pw.error = "영문 또는 숫자와 특수문자 필수\n"
                // 비밀번호의 길이 체크
                if(pw.length !in 6..20)
                    join_pw.error = "비밀번호는 6~20로 생성해주세요"
            }
            override fun afterTextChanged(editable: Editable) {}
        })

        join_pw_ck.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                // 비밀번호와 비밀번호 확인의 값이 동일한지 체크
                if(join_pw.text.toString() != join_pw_ck.text.toString()){
                    join_pw_ck.error = "비밀번호가 일치하지 않습니다."
                    checkPw = false
                }
                else
                    checkPw = true
            }
            override fun afterTextChanged(editable: Editable) {}
        })
    }
}