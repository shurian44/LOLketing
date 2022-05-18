package com.ezen.lolketing.view.main.news

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebViewClient
import com.ezen.lolketing.BaseActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityNewsWebViewBinding
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.startActivity
import com.ezen.lolketing.util.toast
import com.ezen.lolketing.view.login.LoginActivity
import com.ezen.lolketing.view.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NewsWebViewActivity : BaseActivity<ActivityNewsWebViewBinding>(R.layout.activity_news_web_view) {

    @Inject lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val url = intent.getStringExtra(Constants.URL)

        if (url == null){
            toast("웹 사이트에 오류가 발생하였습니다.")
            Handler(mainLooper).postDelayed({ finish() }, 1000)
        }

        binding.newsWebView.apply {
            webViewClient = WebViewClient() // 클릭시 새창 안뜨게
            loadUrl(url!!)
        }

        binding.newsWebView.settings.apply {
            javaScriptEnabled = true // 웹페이지 자바스크립트 허용 여부
            javaScriptCanOpenWindowsAutomatically = false // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
            loadWithOverviewMode = true // 메타태그 허용 여부
            useWideViewPort = true // 화면 사이즈 맞추기 허용 여부
            builtInZoomControls = true // 화면 확대 축소 허용 여부
            layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN // 컨텐츠 사이즈 맞추기
            cacheMode = WebSettings.LOAD_NO_CACHE // 브라우저 캐시 허용 여부
            domStorageEnabled = true // 로컬저장소 허용 여부
            setSupportZoom(false) // 화면 줌 허용 여부
            setSupportMultipleWindows(false) // 새창 띄우기 허용 여부
        }
    }

    override fun logout(view: View) {
        auth.signOut()
        startActivity(LoginActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TOP)
        finish()
    }

    override fun moveHome(view: View) {
        startActivity(MainActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TOP)
        finish()
    }
}