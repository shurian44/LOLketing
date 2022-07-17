package com.ezen.lolketing.view.main.board.search

import android.os.Bundle
import androidx.activity.viewModels
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivitySearchBinding
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.view.custom.CustomEditTextView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : BaseViewModelActivity<ActivitySearchBinding, SearchViewModel>(R.layout.activity_search) {

    override val viewModel: SearchViewModel by viewModels()
    private lateinit var team : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()

    }

    private fun initViews() = with(binding) {
        team = intent?.getStringExtra(Constants.TEAM) ?: ""
        title = team

        layoutTop.btnBack.setOnClickListener { onBackClick(it) }

        editSearch.setDrawableClickListener(CustomEditTextView.DRAWABLE_END) {
        }

        editSearch.setDrawableClickListener(CustomEditTextView.DRAWABLE_START) {
        }
    }

}