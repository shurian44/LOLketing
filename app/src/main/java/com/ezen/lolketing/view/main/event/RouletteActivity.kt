package com.ezen.lolketing.view.main.event

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.animation.doOnEnd
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityRouletteBinding
import com.ezen.lolketing.util.repeatOnStarted
import com.ezen.lolketing.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.util.*

@AndroidEntryPoint
class RouletteActivity : BaseViewModelActivity<ActivityRouletteBinding, RouletteViewModel>(R.layout.activity_roulette) {

    override val viewModel: RouletteViewModel by viewModels()

    var startDegree = 0f
    var endDegree = 0f
    var degreeRand = 0
    var rouletteResult = 0
    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
        repeatOnStarted {
            viewModel.eventFlow.collect { event -> eventHandler(event) }
        }

    }

    private fun initViews() {
        // Animator 설정
        setAnimator()
        // 룰렛 카운트 조회
        viewModel.getRouletteCount()
    }

    private fun eventHandler(event: RouletteViewModel.Event) {
        when(event) {
            is RouletteViewModel.Event.RouletteCount -> {
                count = event.count
                binding.txtCount.text = getString(R.string.roulette_count, event.count)
            }
            is RouletteViewModel.Event.Success -> {
                binding.txtCount.text = getString(R.string.roulette_count, --count)
            }
            is RouletteViewModel.Event.Failure -> {
                toast(getString(R.string.error_unexpected))
                finish()
            }
        }
    }

    // 룰렛 이미지 터치 시에 호출되는 메소드
    fun rotate(v: View?) {
        if (count <= 0) {
            Toast.makeText(this, "기회를 다 소진하였습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        // ---------- 회전각도 설정 ----------
        val rand = Random() // 랜덤 객체 생성
        degreeRand = rand.nextInt(360) // 0~359 사이의 정수 추출
        endDegree = startDegree + 360 * 5 + degreeRand // 회전 종료각도 설정(초기 각도에서 세바퀴 돌고 0~359 난수만큼 회전)
        setAnimator()
    }

    private fun setAnimator() {
        // ---------- 애니메이션 실행 ----------
        // 애니메이션 이미지에 대해 초기 각도에서 회전종료 각도까지 회전하는 애니메이션 객체 생성
        val animator = ObjectAnimator.ofFloat(binding.imgRoulette, "rotation", startDegree, endDegree)

        animator.apply {
            interpolator = AccelerateDecelerateInterpolator() // 애니메이션 속력 설정
            duration = 5000 // 애니메이션 시간(5초)
            startDegree = endDegree // 이전 정지 각도를 시작 각도로 설정
            doOnEnd {
                rouletteResult = when (degreeRand) {
                    in 225..269 -> {
                        250
                    }
                    in 45..89 -> {
                        300
                    }
                    in 90..134 -> {
                        350
                    }
                    in 270..314 -> {
                        450
                    }
                    in 315..359 -> {
                        550
                    }
                    in 180..224 -> {
                        1000
                    }
                    else -> {
                        2000
                    }
                }
                toast(getString(R.string.roulette_result, rouletteResult))
                viewModel.setCoupon(rouletteResult)
            }
            start() // 애니메이션 시작
        }

    }

}