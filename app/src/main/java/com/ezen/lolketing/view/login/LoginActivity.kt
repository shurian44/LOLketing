package com.ezen.lolketing.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.ezen.lolketing.view.login.join.JoinActivity
import com.ezen.lolketing.view.main.MainActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityLoginBinding
import com.ezen.lolketing.model.Users
import com.ezen.lolketing.BaseActivity
import com.ezen.lolketing.util.toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private val RC_SIGN_IN = 1000   // 구글 로그인 확인 코드
    private var auth = FirebaseAuth.getInstance()
    private var firestore = FirebaseFirestore.getInstance()
    private lateinit var googleSignInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 자동 로그인 : 로그인 상태일 시 바로 메인 페이지로 이동
        if( auth.currentUser != null)
            moveMain()

        // 구글 로그인 옵션
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        binding.btnGoogleLogin.setOnClickListener {
            googleLogin(it)
        }

    } // onCreate()

    // 회원가입 버튼 클릭 : 회원가입 페이지로 이동
    fun createUser(view: View) {
        var intent = Intent(this, JoinActivity::class.java)
        startActivity(intent)
    } // createUser()

    // 이메일 로그인 버튼 클릭
    fun emailLogin(view: View) {
        var id = binding.loginId.text.toString()
        var pw = binding.loginPw.text.toString()

        // 아이디 또는 비밀번호를 입력하지 않았을 시
        if(id.isEmpty() || pw.isEmpty()){
            toast("아이디 또는 패스워드를 입력해주세요")
            return
        }
        auth.signInWithEmailAndPassword(id, pw).addOnCompleteListener {
            if(it.isSuccessful){    // 로그인 성공
                moveMain()
            }
            else{   // 로그인 실패
                binding.loginPw.text = null
                toast("아이디 또는 비밀번호를 확인해주세요")
            }
        }
    } // emailLogin()

    // 구글 로그인 버튼 클릭
    fun googleLogin(view: View) {
//        toast("서비스 오류로인해 수정 준비중입니다.")
//        return
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    } // googleLogin()

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Log.e("구글 로그인", "실패임 ${e.printStackTrace()}")
                e.printStackTrace()
            }
        }
    } // onActivityResult()

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) { // 구글 로그인 성공
                    firestore.collection("Users").document(auth.currentUser?.email!!).addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                        if(documentSnapshot?.data == null){ // 첫 로그인(회원가입) 경우 아이디 생성
                            var email = auth.currentUser?.email!!
                            var user = Users()
                            user.id = email
                            firestore.collection("Users").document(email).set(user)
                        }
                        else{ // 회원가입한 상태일 경우 페이지 이동
                            moveMain()
                        }
                    }

                } else { // 구글 로그인 실패
                    toast("구글 로그인을 실패하였습니다.")
                }
            }
    } // firebaseAuthWithGoogle()

    // 메인 페이지로 이동
    private fun moveMain(){
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    } // moveMain()

    override fun logout(view: View) {}
}