package com.ezen.lolketing.view.main.guide

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import com.ezen.lolketing.BaseActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.adapter.EventSliderAdapter
import com.ezen.lolketing.databinding.ActivityLolGuideDetailBinding
import com.ezen.lolketing.util.htmlFormat
import com.ezen.lolketing.util.startActivity
import com.ezen.lolketing.view.login.LoginActivity
import com.ezen.lolketing.view.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView

class LoLGuideDetailActivity : BaseActivity<ActivityLolGuideDetailBinding>(R.layout.activity_lol_guide_detail) {

    private var auth = FirebaseAuth.getInstance()
    lateinit var status : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()

    }

    fun initViews() = with(binding) {

        activity = this@LoLGuideDetailActivity

        status = intent?.getStringExtra("status") ?: ""
        when(status){
            AOS->{
                title = getString(R.string.aos)
                guideImg.setImageResource(R.drawable.lol_guide_aos)
                txtContent.text = getString(R.string.guide_aos).htmlFormat()
            }
            RULE->{
                title = getString(R.string.rule)
                guideImg.setImageResource(R.drawable.lol_guide_rule)
                txtContent.text = getString(R.string.guide_rule).htmlFormat()
            }
            POSITION->{
                title = getString(R.string.position)
                guideImg.setImageResource(R.drawable.lol_guide_position)
                txtContent.text = getString(R.string.guide_position).htmlFormat()
            }
            NATURE->{
                title = getString(R.string.nature)
                guideImg.setImageResource(R.drawable.lol_guide_nature)
                txtContent.text = getString(R.string.guide_nature).htmlFormat()
            }
            SCORE->{
                title = getString(R.string.score)
                guideImg.setImageResource(R.drawable.lol_guide_score)
                txtContent.text = getString(R.string.guide_score).htmlFormat()
            }
            TERMS->{
                title = getString(R.string.game_terms)
                guideImg.setImageResource(R.drawable.lol_guide_term)
                imageSlider.visibility = View.VISIBLE
                txtContent.visibility = View.GONE
                setImageSlider(imageSlider)
            }
        }
    }

    private fun setImageSlider(imageSlider: SliderView) = with(imageSlider){
        val images = listOf(
            R.drawable.img_terms01,
            R.drawable.img_terms02,
            R.drawable.img_terms03,
            R.drawable.img_terms04,
            R.drawable.img_terms05,
            R.drawable.img_terms06,
            R.drawable.img_terms07,
            R.drawable.img_terms08,
            R.drawable.img_terms09,
            R.drawable.img_terms10,
            R.drawable.img_terms11,
            R.drawable.img_terms12,
            R.drawable.img_terms13,
            R.drawable.img_terms14
        )
        setSliderAdapter(EventSliderAdapter(images))
        startAutoCycle()
        setIndicatorAnimation(IndicatorAnimations.WORM)
        setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
    }

    /** 로그아웃 버튼 클릭 **/
    override fun logout(view: View) {
        auth.signOut()
        startActivity(LoginActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TOP)
        finish()
    }

    /** 관리자 페이지 버튼 클릭 **/
    override fun moveHome(view: View) {
        startActivity(MainActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TOP)
        finish()
    }

    companion object {
        const val AOS = "aos"
        const val RULE = "rule"
        const val POSITION = "position"
        const val NATURE = "nature"
        const val SCORE = "score"
        const val TERMS = "terms"
    }

}