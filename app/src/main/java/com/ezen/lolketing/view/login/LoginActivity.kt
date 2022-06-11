package com.ezen.lolketing.view.login

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.ezen.lolketing.view.login.join.JoinActivity
import com.ezen.lolketing.view.main.MainActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityLoginBinding
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.util.repeatOnStarted
import com.ezen.lolketing.util.startActivity
import com.ezen.lolketing.util.toast
import com.ezen.lolketing.view.login.find.FindIdPwActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LoginActivity : BaseViewModelActivity<ActivityLoginBinding, LoginViewModel>(R.layout.activity_login) {

    override val viewModel: LoginViewModel by viewModels()
    private lateinit var googleSignInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.activity = this

        repeatOnStarted {
            viewModel.eventFlow.collect{ event-> eventHandler(event) }
        }

        viewModel.autoLogin()

    } // onCreate()

    private fun eventHandler(event : LoginViewModel.Event) {
        when(event) {
            is LoginViewModel.Event.AutoLoginSuccess, LoginViewModel.Event.LoginSuccess,
            LoginViewModel.Event.UserInfoSuccess, LoginViewModel.Event.RegisterSuccess -> {
                moveMain()
            }
            // 자동 로그인 실패 : 최초 로그인 또는 로그아웃한 경우
            is LoginViewModel.Event.AutoLoginFailure -> {
                // 구글 로그인 옵션
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
                googleSignInClient = GoogleSignIn.getClient(this, gso)
            }
            // 로그인 실패
            is LoginViewModel.Event.LoginFailure -> {
                binding.loginPw.text = null
                toast(event.msg)
            }
            // 구글 로그인 했을 때 유저 정보가 없으면 신규 가입으로 보고 가입 절차 진행
            is LoginViewModel.Event.UserInfoFailure -> {
                viewModel.registerUser(event.email)
            }
            // 구글 로그인 성공 후 등록 과정 중 문제가 발생했을 경우 대비
            is LoginViewModel.Event.RegisterFailure -> {
                viewModel.deleteUser()
            }
        }
    }

    /** 회원가입 버튼 클릭 : 회원가입 페이지로 이동 **/
    fun createUser(view: View) {
        startActivity(JoinActivity::class.java)
    } // createUser()

    /** 이메일 로그인 버튼 클릭 **/
    fun emailLogin(view: View) = with(binding) {
        val id = loginId.text.toString()
        val pw = loginPw.text.toString()

        // 아이디 또는 비밀번호를 입력하지 않았을 시
        if(id.isEmpty() || pw.isEmpty()){
            toast(getString(R.string.guide_input_id_pw))
            return@with
        }

        viewModel.emailLogin(
            id = id,
            pw = pw,
            failureMsg = getString(R.string.guide_check_id_pw)
        )
    } // emailLogin()

    /** 구글 로그인 버튼 클릭 **/
    fun googleLogin(view: View) {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    } // googleLogin()

    /** 메인 페이지로 이동 **/
    private fun moveMain(){
        startActivity(MainActivity::class.java)
        finish()
    } // moveMain()

    /** Id/Pw 찾기 페이지로 이동 **/
    fun moveFind(view: View) {
        startActivity(FindIdPwActivity::class.java)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java) ?: run {
                    toast(getString(R.string.guide_google_login_fail))
                    return@registerForActivityResult
                }
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                e.printStackTrace()
                toast(getString(R.string.guide_google_login_fail))
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        viewModel.googleLogin(credential, getString(R.string.guide_google_login_fail))
    } // firebaseAuthWithGoogle()

}