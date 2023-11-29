package com.ezen.lolketing.view.login.join

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityJoinBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class JoinActivity :
    StatusViewModelActivity<ActivityJoinBinding, JoinViewModel>(R.layout.activity_join) {

    override val viewModel: JoinViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
    }

    /** 각종 뷰들 초기화 **/
    private fun initViews() = with(binding) {
        vm = viewModel

        editJoinId.addTextChangedListener {
            // 입력한 내용이 비어있는지 이메일 형식이 맞는지 체크
            val status = editJoinId.text.isNullOrEmpty() ||
                    !android.util.Patterns.EMAIL_ADDRESS.matcher(editJoinId.text.toString())
                        .matches()

            editJoinId.setStateError(status)
            txtTipId.isVisible = status
        }

        editJoinPw.addTextChangedListener {
            // 영문, 숫자, 특수 문자가 모두 포함하여 6~20자가 맞는지 체크
            val pattern = "\\w+"
            val pw: String = binding.editJoinPw.text.toString()
            val status = Pattern.matches(pattern, pw) || pw.length !in 6..20

            editJoinPw.setStateError(status)
            txtTipPassWord.isVisible = status
        }

        editJoinPwCheck.addTextChangedListener {
            // 비밀번호와 비밀번호 확인이 일치하는지 체크
            val status = editJoinPw.text.toString() != editJoinPwCheck.text.toString()

            editJoinPwCheck.setStateError(status)
            txtTipCheck.isVisible = status
        }
    }

}