package com.ezen.lolketing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.ezen.lolketing.model.Users
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity() {
    private val RC_SIGN_IN = 1000   // 구글 로그인 확인 코드
    private var auth = FirebaseAuth.getInstance()   // 계정 관리 변수
    private var firestore = FirebaseFirestore.getInstance() // Cloud Firestore 변수
    private lateinit var googleSignInClient : GoogleSignInClient    // 구글 클라이언트

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 자동 로그인 : 로그인 상태일 시 바로 로그인 페이지로 이동
        if( auth.currentUser != null) moveMain()

        // 구글 로그인 옵션
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

    } // onCreate

    // 메인 페이지로 이동
    private fun moveMain(){
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    // 유저 회원가입
    fun createUser(view: View) {
        var intent = Intent(this, JoinActivity::class.java)
        startActivity(intent)
    }

    // 이메일 로그인
    fun emailLogin(view: View) {
        // 아이디 또는 비밀번호를 입력하지 않았을 시
        if(login_id.text.toString().isEmpty() || login_pw.text.toString().isEmpty()){
            toast("아이디 또는 패스워드를 입력해주세요")
            return
        }
        auth.signInWithEmailAndPassword(login_id.text.toString(), login_pw.text.toString()).addOnCompleteListener {
            if(it.isSuccessful){    // 로그인 성공
                moveMain()
            }
            else{   // 로그인 실패
                Toast.makeText(this, "아이디 또는 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 구글 로그인
    fun googleLogin(view: View) {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                e.printStackTrace()
            }
        }
    }

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

                } else {
                    Toast.makeText(this, "구글 로그인을 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}