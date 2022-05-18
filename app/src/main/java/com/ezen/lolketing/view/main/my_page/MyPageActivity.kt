package com.ezen.lolketing.view.main.my_page

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.LayoutRes
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.view.main.MainActivity
import com.ezen.lolketing.view.main.shop.PurchaseHistoryActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.WithdrawalActivity
import com.ezen.lolketing.databinding.ActivityMyPageBinding
import com.ezen.lolketing.model.Users
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.util.repeatOnStarted
import com.ezen.lolketing.util.startActivity
import com.ezen.lolketing.view.login.LoginActivity
import com.ezen.lolketing.view.login.join.JoinDetailActivity
import com.ezen.lolketing.view.main.my_page.cache.CacheChargingActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class MyPageActivity : BaseViewModelActivity<ActivityMyPageBinding, MyPageViewModel>(R.layout.activity_my_page) {

    override val viewModel: MyPageViewModel by viewModels()

    @Inject lateinit var auth : FirebaseAuth
    private lateinit var id : String
    private lateinit var grade : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.activity = this
        repeatOnStarted {
            viewModel.eventFlow.collect { event-> eventHandler(event) }
        }

    }

    private fun eventHandler(event: MyPageViewModel.Event) {
        when(event) {
            is MyPageViewModel.Event.CouponCounter -> {
                binding.txtCoupon.text = "${event.numberOfCoupon} 장"
            }
            is MyPageViewModel.Event.UserInfo -> {
                initViews(event.user)
            }
        }
    }

    private fun initViews(user : Users) = with(binding) {
        // 마이페이지 UI에 회원 정보 보여주기
        grade = user.grade!!
        txtGrade.text = grade
        txtPoint.text = "${user.point}"
        txtAccPoint.text = "${user.accPoint}"
        txtCache.text = "${user.cache}"
        txtName.text = user.nickname

        // 등급에 따라 아이콘과 프로그래스 바를 설정
        when(grade){
            Constants.BRONZE->{
                setGradeViews(R.drawable.icon_bronze, 3000, "3천 P")
            }
            Constants.SILVER-> {
                setGradeViews(R.drawable.icon_silver, 30000, "3만 P")
            }
            Constants.GOLD-> {
                setGradeViews(R.drawable.icon_gold, 300000, "30만 P")
            }
            Constants.PLATINUM-> {
                setGradeViews(R.drawable.icon_platinum, 0, "max")
            }
            Constants.MASTER-> {
                setGradeViews(R.drawable.icon_master, 0, "max")
            }
        }
        progressGrade.progress = user.accPoint!!
    }

    private fun setGradeViews(@LayoutRes gradeImgRes: Int, maxProgress: Int, gradeGuide: String) = with(binding) {
        imgGrade.setImageResource(gradeImgRes)
        progressGrade.max = maxProgress
        txtGradeGauge.text = gradeGuide
    }

    // 각 버튼별 페이지 이동
    fun moveActivity(view: View) {
        when(view.id){
            R.id.txt_Cache -> startActivity(CacheChargingActivity::class.java)
            R.id.btn_history -> startActivity(PurchaseHistoryActivity::class.java)
            R.id.txt_withdrawal -> startActivity(WithdrawalActivity::class.java)
            R.id.txt_coupon -> startActivity(CouponActivity::class.java)
            R.id.txt_grade_detail -> startActivity(MembershipActivity::class.java)
            R.id.btn_modify ->{
                startActivity(createIntent(JoinDetailActivity::class.java).also {
                    it.putExtra("modify", "modify")
                })
            }
        }
    }

    override fun logout(view: View) {
        auth.signOut()
        startActivity(LoginActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TOP)
        finish()
    }

    override fun moveHome(view: View) {
        startActivity(MainActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TOP)
        finish()
    }
}