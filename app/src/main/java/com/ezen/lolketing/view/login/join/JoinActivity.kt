package com.ezen.lolketing.view.login.join

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityJoinBinding
import com.ezen.lolketing.util.repeatOnStarted
import com.ezen.lolketing.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.util.regex.Pattern

@AndroidEntryPoint
class JoinActivity : BaseViewModelActivity<ActivityJoinBinding, JoinViewModel>(R.layout.activity_join) {

    override val viewModel: JoinViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repeatOnStarted {
            viewModel.eventFlow.collect{ event -> eventHandler(event) }
        }

        initViews()
    }

    /** 각종 뷰들 초기화 **/
    private fun initViews() = with(binding) {
        activity = this@JoinActivity

        editJoinId.addTextChangedListener {
            // 입력한 내용이 비어있는지 이메일 형식이 맞는지 체크
            val status = editJoinId.text.isNullOrEmpty() ||
                    !android.util.Patterns.EMAIL_ADDRESS.matcher(editJoinId.text.toString()).matches()

            editJoinId.setStateError(status)
            if (status) setTip(getString(R.string.guide_create_id)) else setTip("")
        }

        editJoinPw.addTextChangedListener {
            // 영문, 숫자, 특수 문자가 모두 포함하여 6~20자가 맞는지 체크
            val pattern = "\\w+"
            val pw: String = binding.editJoinPw.text.toString()
            val status = Pattern.matches(pattern, pw) || pw.length !in 6..20

            editJoinPw.setStateError(status)
            if (status) setTip(getString(R.string.guide_create_pw)) else setTip("")
        }

        editJoinPwCheck.addTextChangedListener {
            // 비밀번호와 비밀번호 확인이 일치하는지 체크
            val status = editJoinPw.text.toString() != editJoinPwCheck.text.toString()

            editJoinPwCheck.setStateError(status)
            if (status) setTip(getString(R.string.guide_create_pw_check)) else setTip("")
        }
    }

    /** EditText 정보 입력 시의 팁 표시 **/
    private fun setTip(msg: String) {
        binding.txtTip.text = msg
    }

    private fun eventHandler(event: JoinViewModel.Event) {
        when(event) {
            is JoinViewModel.Event.Success -> {
                toast(getString(R.string.join_success))
                finish()
            }
            is JoinViewModel.Event.Error -> {
                if(event.code == JoinViewModel.ALREADY_JOIN) {
                    toast(getString(R.string.already_join))
                } else {
                    toast(getString(R.string.join_failure))
                }
            }
        }
    }

    /** 이메일 회원가입 **/
    fun emailJoin(view: View) = with(binding) {
        val email = editJoinId.text.toString()
        val pw = editJoinPw.text.toString()
        val pwCheck = editJoinPwCheck.text.toString()

        // 입력 양식에 맞지 않거나 내용을 입력하지 않았을 경우
        if( editJoinId.getStateError() || editJoinPw.getStateError() || editJoinPwCheck.getStateError()
            || email.isEmpty() || pw.isEmpty() || pwCheck.isEmpty()){
            toast(getString(R.string.guide_input_check))
            return@with
        }

        // 비밀번호와 비밀번호 채크가 같을 경우
        if(pw == pwCheck){
            // 이메일 회원가입
            viewModel.joinUser(email, pw)
        } else{ // 비밀번호와 비밀번호 채크가 일치하지 않을 경우
            toast(getString(R.string.guide_check_pw))
            return
        }
    }

}