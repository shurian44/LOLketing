package com.ezen.lolketing.view.main.event

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.viewModels
import androidx.core.animation.doOnEnd
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityRouletteBinding
import com.ezen.lolketing.util.repeatOnCreated
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class RouletteActivity :
    StatusViewModelActivity<ActivityRouletteBinding, RouletteViewModel>(R.layout.activity_roulette) {

    override val viewModel: RouletteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()

        repeatOnCreated {
            viewModel.uiStatus.collectLatest {
                if (it is RouletteUiStatus.RouletteStart) {
                    setAnimator(it.deg)
                }
            }
        }
    }

    /** 각종 뷰들 초기화 **/
    private fun initViews() = with(binding) {
        activity = this@RouletteActivity
        vm = viewModel

        layoutTop.btnBack.setOnClickListener { finish() }
    }

    /** 룰렛 회전 **/
    private fun setAnimator(degreeRand: Float) {
        // ---------- 회전각도 설정 ----------
        val endDegree = 0 + 360 * 5 + degreeRand // 회전 종료각도 설정(초기 각도에서 세바퀴 돌고 0~359 난수만큼 회전)

        // ---------- 애니메이션 실행 ----------
        // 애니메이션 이미지에 대해 초기 각도에서 회전종료 각도까지 회전하는 애니메이션 객체 생성
        val animator =
            ObjectAnimator.ofFloat(binding.imgRoulette, "rotation", 0f, endDegree)

        animator.apply {
            interpolator = AccelerateDecelerateInterpolator() // 애니메이션 속력 설정
            duration = 5000 // 애니메이션 시간(5초)
            start() // 애니메이션 시작
            doOnEnd {
                viewModel.insertRouletteCoupon()
            } // doOnEnd
        } // animator.apply
    } // setAnimator
}