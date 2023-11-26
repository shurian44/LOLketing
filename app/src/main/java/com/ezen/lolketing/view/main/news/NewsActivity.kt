package com.ezen.lolketing.view.main.news

import android.os.Bundle
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.adapter.NewsAdapter
import com.ezen.lolketing.databinding.ActivityNewsBinding
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.addOnScrollListener
import com.ezen.lolketing.util.createIntent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsActivity : StatusViewModelActivity<ActivityNewsBinding, NewsViewModel>(R.layout.activity_news) {

    override val viewModel: NewsViewModel by viewModels()
    val adapter = NewsAdapter { url ->
        startActivity(
            createIntent(NewsWebViewActivity::class.java).also {
                it.putExtra(Constants.URL, url)
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
    }

    /** 각종 뷰들 초기화 **/
    private fun initViews() = with(binding) {
        layoutTop.btnBack.setOnClickListener { finish() }
        adapter = this@NewsActivity.adapter
        vm = viewModel

        recyclerView.addOnScrollListener {
            if (viewModel.isMore && !it.canScrollVertically(1) ) {
                viewModel.fetchNews()
            }
        }
    }
}