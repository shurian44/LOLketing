package com.ezen.lolketing.view.main.guide

import android.os.Bundle
import com.ezen.lolketing.BaseActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityLolGuideDetailBinding
import com.ezen.lolketing.util.htmlFormat

class LoLGuideDetailActivity : BaseActivity<ActivityLolGuideDetailBinding>(R.layout.activity_lol_guide_detail) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()

    }

    fun initViews() = with(binding) {

        activity = this@LoLGuideDetailActivity
        layoutTop.btnBack.setOnClickListener { finish() }

        when(intent?.getStringExtra(STATUS) ?: ""){
            AOS->{
                title = getString(R.string.aos)
                imgGuide.setImageResource(R.drawable.lol_guide_aos)
                txtContent.text = getString(R.string.guide_aos).htmlFormat()
            }
            RULE->{
                title = getString(R.string.rule)
                imgGuide.setImageResource(R.drawable.lol_guide_rule)
                txtContent.text = getString(R.string.guide_rule).htmlFormat()
            }
            POSITION->{
                title = getString(R.string.position)
                imgGuide.setImageResource(R.drawable.lol_guide_position)
                txtContent.text = getString(R.string.guide_position).htmlFormat()
            }
            NATURE->{
                title = getString(R.string.nature)
                imgGuide.setImageResource(R.drawable.lol_guide_nature)
                txtContent.text = getString(R.string.guide_nature).htmlFormat()
            }
            SCORE->{
                title = getString(R.string.score)
                imgGuide.setImageResource(R.drawable.lol_guide_score)
                txtContent.text = getString(R.string.guide_score).htmlFormat()
            }
        }
    }

    companion object {
        const val STATUS = "status"
        const val AOS = "aos"
        const val RULE = "rule"
        const val POSITION = "position"
        const val NATURE = "nature"
        const val SCORE = "score"
    }

}