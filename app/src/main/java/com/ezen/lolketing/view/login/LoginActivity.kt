package com.ezen.lolketing.view.login

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.ezen.lolketing.view.login.join.JoinActivity
import com.ezen.lolketing.view.main.MainActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityLoginBinding
import com.ezen.lolketing.model.Users
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.repeatOnStarted
import com.ezen.lolketing.util.startActivity
import com.ezen.lolketing.util.toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseViewModelActivity<ActivityLoginBinding, LoginViewModel>(R.layout.activity_login) {

    override val viewModel: LoginViewModel by viewModels()
    @Inject lateinit var auth : FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient
    private var firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 자동 로그인 : 로그인 상태일 시 바로 메인 페이지로 이동
        if( auth.currentUser != null)
            moveMain()

        binding.activity = this

        // 구글 로그인 옵션
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        repeatOnStarted {
            viewModel.eventFlow.collect{ event-> eventHandler(event) }
        }


    } // onCreate()

    // 회원가입 버튼 클릭 : 회원가입 페이지로 이동
    fun createUser(view: View) {
        startActivity(JoinActivity::class.java)
    } // createUser()

    // 이메일 로그인 버튼 클릭
    fun emailLogin(view: View) = with(binding) {
        val id = loginId.text.toString()
        val pw = loginPw.text.toString()

        // 아이디 또는 비밀번호를 입력하지 않았을 시
        if(id.isEmpty() || pw.isEmpty()){
            toast(getString(R.string.guide_input_id_pw))
            return@with
        }

        auth.signInWithEmailAndPassword(id, pw)
            .addOnSuccessListener {
                moveMain()
            }
            .addOnFailureListener {
                it.printStackTrace()
                loginPw.text = null
                toast(getString(R.string.guide_check_id_pw))
            }
    } // emailLogin()

    // 구글 로그인 버튼 클릭
    fun googleLogin(view: View) {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    } // googleLogin()

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                val email = auth.currentUser?.email
                if (email.isNullOrEmpty()) {
                    toast(getString(R.string.guide_google_login_fail))
                    return@addOnSuccessListener
                }
                viewModel.getUserInfo(email)

            }
            .addOnFailureListener {
                toast(getString(R.string.guide_google_login_fail))
            }
    } // firebaseAuthWithGoogle()

    // 메인 페이지로 이동
    private fun moveMain(){
        startActivity(MainActivity::class.java)
        finish()
    } // moveMain()

    private fun eventHandler(event : LoginViewModel.Event) {
        when(event) {
            is LoginViewModel.Event.UserInfoSuccess -> {
                moveMain()
            }
            is LoginViewModel.Event.UserInfoFailure -> {
                viewModel.registerUser(event.email)
            }
            is LoginViewModel.Event.RegisterSuccess -> {
                moveMain()
            }
            is LoginViewModel.Event.RegisterFailure -> {
                viewModel.deleteUser()
            }
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                e.printStackTrace()
                toast(getString(R.string.guide_google_login_fail))
            }
        }
    }

}