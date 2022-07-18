package com.ezen.lolketing.view.main.news

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.adapter.NewsAdapter
import com.ezen.lolketing.databinding.ActivityNewsBinding
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.util.repeatOnStarted
import com.ezen.lolketing.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsActivity : BaseViewModelActivity<ActivityNewsBinding, NewsViewModel>(R.layout.activity_news) {

    override val viewModel: NewsViewModel by viewModels()
    private lateinit var adapter : NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repeatOnStarted {
            viewModel.eventFlow.collect { event -> eventHandler(event) }
        }

        initViews()
        viewModel.getNews()
    }

    /** 각종 뷰들 초기화 **/
    private fun initViews() = with(binding) {
        title = getString(R.string.news)
        layoutTop.btnBack.setOnClickListener { onBackClick(it) }
        adapter = NewsAdapter { url ->
            startActivity(
                createIntent(NewsWebViewActivity::class.java).also {
                    it.putExtra(Constants.URL, url)
                }
            )
        }
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (viewModel.isMore && !recyclerView.canScrollVertically(1) ) {
                    viewModel.getNews()
                }
            }
        })
    }

    private fun eventHandler(event: NewsViewModel.Event) {
        when(event) {
            is NewsViewModel.Event.Failure -> {
                toast(getString(R.string.error_news_search))
            }
            is NewsViewModel.Event.Success -> {
                adapter.addNewsItems(event.result)
            }
        }
    }

}