package com.ezen.lolketing.view.main.news

import android.os.Bundle
import android.os.Handler
import android.webkit.WebSettings
import android.webkit.WebViewClient
import com.ezen.lolketing.BaseActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityNewsWebViewBinding
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsWebViewActivity : BaseActivity<ActivityNewsWebViewBinding>(R.layout.activity_news_web_view) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()

    }

    private fun initViews() = with(binding) {
        title = getString(R.string.news)
        layoutTop.btnBack.setOnClickListener { onBackClick(it) }

        val url = intent.getStringExtra(Constants.URL)

        if (url == null){
            toast("웹 사이트에 오류가 발생하였습니다.")
            Handler(mainLooper).postDelayed({ finish() }, 1000)
            return@with
        }

        newsWebView.apply {
            webViewClient = WebViewClient() // 클릭시 새창 안뜨게
            loadUrl(url)
        }

        newsWebView.settings.apply {
            javaScriptEnabled = true // 웹페이지 자바스크립트 허용 여부
            javaScriptCanOpenWindowsAutomatically = false // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
            loadWithOverviewMode = true // 메타태그 허용 여부
            useWideViewPort = true // 화면 사이즈 맞추기 허용 여부
            builtInZoomControls = true // 화면 확대 축소 허용 여부
            cacheMode = WebSettings.LOAD_NO_CACHE // 브라우저 캐시 허용 여부
            domStorageEnabled = true // 로컬저장소 허용 여부
            setSupportZoom(false) // 화면 줌 허용 여부
            setSupportMultipleWindows(false) // 새창 띄우기 허용 여부
        }
    }

    // 뒤로 가기 제어
    override fun onBackPressed() {
        // 웹뷰가 뒤로 가기가 가능한지 여부를 체크하여 웹뷰 뒤로가기 또는 액티비티 뒤로가기를 실행합니다.
        if (binding.newsWebView.canGoBack()) {
            binding.newsWebView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}