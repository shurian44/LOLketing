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

class LoginActivity : AppCompatActivity() {
    private val RC_SIGN_IN = 1000
    private var auth = FirebaseAuth.getInstance()
    private var firestore = FirebaseFirestore.getInstance()
    private lateinit var googleSignInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if( auth != null) moveMain()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

    }

    // 메인 페이지로 이동
    private fun moveMain(){
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    // 유저 회원가입
    fun createUser(view: View) {
        var intent = Intent(this, JoinActivity::class.java)
        startActivity(intent)
    }

    // 이메일 로그인
    fun emailLogin(view: View) {
        auth.signInWithEmailAndPassword(login_id.text.toString(), login_pw.text.toString()).addOnCompleteListener {
            if(it.isSuccessful){
                moveMain()
            }
            else{
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
                if (task.isSuccessful) {
                    var email = auth.currentUser?.email
                    var user = Users()
                    user.id = email

                    firestore.collection("Users").document(email!!).addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                        if(documentSnapshot?.data == null){
                            firestore.collection("Users").document(email).set(user)
                        }
                    }

                    moveMain()
                } else {
                    Toast.makeText(this, "구글 로그인을 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}