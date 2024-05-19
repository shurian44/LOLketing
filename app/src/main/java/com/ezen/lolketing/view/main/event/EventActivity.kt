package com.ezen.lolketing.view.main.event

import android.os.Bundle
import androidx.activity.viewModels
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityEventBinding
import com.ezen.lolketing.util.repeatOnCreated
import com.ezen.lolketing.util.startActivity
import com.ezen.lolketing.view.dialog.CommonDialogItem
import com.ezen.lolketing.view.dialog.ConfirmDialog
import com.ezen.lolketing.view.main.my_page.MyPageActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class EventActivity : BaseViewModelActivity<ActivityEventBinding, EventViewModel>(R.layout.activity_event) {

    override val viewModel: EventViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()

        repeatOnCreated {
            viewModel.uiStatus.collectLatest {
                when(it) {
                    EventUIStatus.Init -> {}
                    EventUIStatus.IssuedCoupon -> {
                        showIssuedCouponDialog()
                        viewModel.updateInitStatus()
                    }
                    EventUIStatus.Success -> {
                        showSuccessDialog()
                        viewModel.updateInitStatus()
                    }
                }
            }
        }
    }

    /** 각종 뷰들 초기화 **/
    private fun initViews() = with(binding) {
        activity = this@EventActivity
        vm = viewModel

        layoutTop.btnBack.setOnClickListener { onBackClick(it) }
    }

    fun goToRoulette() {
        startActivity(RouletteActivity::class.java)
    }

    private fun showIssuedCouponDialog() {
        ConfirmDialog(
            CommonDialogItem(
                message = "이미 쿠폰을 발급받으셨습니다.\n내 정보에서 쿠폰을 확인해주세요.",
                isSingleButton = true
            )
        ).show(supportFragmentManager, "")
    }

    private fun showSuccessDialog() {
        ConfirmDialog(
            CommonDialogItem(
                title = "신규 가입 쿠폰 발급 완료",
                message = "내 정보에서 발급받은 쿠폰을\n확인할 수 있습니다.\n바로 이동하시겠습니까?",
                onOkClick = {
                    startActivity(MyPageActivity::class.java)
                }
            )
        ).show(supportFragmentManager, "")
    }

    override fun onResume() {
        super.onResume()

        viewModel.fetchNewUserCoupon()
    }

}