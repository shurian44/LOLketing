package com.ezen.lolketing.view.main.guide

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.ezen.lolketing.BaseActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityGuideBinding
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.view.ui.theme.Black
import com.ezen.lolketing.view.ui.theme.LOLketingTheme

class GuideActivity : BaseActivity<ActivityGuideBinding>(R.layout.activity_guide) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//                const val STATUS = "status"
//        const val AOS = "aos"
//        const val RULE = "rule"
//        const val POSITION = "position"
//        const val NATURE = "nature"
//        const val SCORE = "score"
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
            binding.cardTerms.id -> {}
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