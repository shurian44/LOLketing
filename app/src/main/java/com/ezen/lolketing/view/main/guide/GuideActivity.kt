package com.ezen.lolketing.view.main.guide

import android.os.Bundle
import android.view.View
import com.ezen.lolketing.BaseActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityGuideBinding
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.util.startActivity

class GuideActivity : BaseActivity<ActivityGuideBinding>(R.layout.activity_guide) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.activity = this
    }

    fun onClick(view: View) {
        when(view.id) {
            binding.cardAos.id -> {
                goToDetail(LoLGuideDetailActivity.AOS)
            }
            binding.cardRule.id -> {
                goToDetail(LoLGuideDetailActivity.RULE)
            }
            binding.cardPosition.id -> {
                goToDetail(LoLGuideDetailActivity.POSITION)
            }
            binding.cardNature.id -> {
                goToDetail(LoLGuideDetailActivity.NATURE)
            }
            binding.cardScore.id -> {
                goToDetail(LoLGuideDetailActivity.SCORE)

            }
            binding.cardTerms.id -> {
                startActivity(GuideTermsActivity::class.java)
            }
        }
    }

    private fun goToDetail(value: String) {
        startActivity(
            createIntent(LoLGuideDetailActivity::class.java).also {
                it.putExtra(LoLGuideDetailActivity.STATUS, value)
            }
        )
    }
}