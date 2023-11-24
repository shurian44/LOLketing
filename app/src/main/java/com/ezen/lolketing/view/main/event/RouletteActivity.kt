package com.ezen.lolketing.view.main.event

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.viewModels
import androidx.core.animation.doOnEnd
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityRouletteBinding
import com.ezen.lolketing.util.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class RouletteActivity :
    StatusViewModelActivity<ActivityRouletteBinding, RouletteViewModel>(R.layout.activity_roulette) {

    override val viewModel: RouletteViewModel by viewModels()

    private var startDegree = 0f
    private var endDegree = 0f
    private var degreeRand = 0
    private var rouletteResult = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
        repeatOnStarted {
            viewModel.isAnimationStart.collectLatest {
                if (it) setAnimator()
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
    private fun setAnimator() {
        // ---------- 회전각도 설정 ----------
        degreeRand = (0..359).random() // 0~359 사이의 정수 추출
        endDegree = startDegree + 360 * 5 + degreeRand // 회전 종료각도 설정(초기 각도에서 세바퀴 돌고 0~359 난수만큼 회전)

        // ---------- 애니메이션 실행 ----------
        // 애니메이션 이미지에 대해 초기 각도에서 회전종료 각도까지 회전하는 애니메이션 객체 생성
        val animator =
            ObjectAnimator.ofFloat(binding.imgRoulette, "rotation", startDegree, endDegree)

        animator.apply {
            interpolator = AccelerateDecelerateInterpolator() // 애니메이션 속력 설정
            duration = 5000 // 애니메이션 시간(5초)
            startDegree = 0f
            start() // 애니메이션 시작
            doOnEnd {
                rouletteResult = when (degreeRand) {
                    in 0..45 -> 2000
                    in 46..90 -> 300
                    in 91..135 -> 350
                    in 136..180 -> 200
                    in 181..225 -> 1000
                    in 226..270 -> 250
                    in 271..315 -> 450
                    else -> 250
                }
                viewModel.couponIssuance(rouletteResult)
            } // doOnEnd
        } // animator.apply
    } // setAnimator
}