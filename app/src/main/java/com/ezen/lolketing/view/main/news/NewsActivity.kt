package com.ezen.lolketing.view.main.news

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ezen.lolketing.BaseActivity
import com.ezen.lolketing.view.main.MainActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.adapter.NewsAdapter
import com.ezen.lolketing.databinding.ActivityNewsBinding
import com.ezen.lolketing.model.NewsItem
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.util.startActivity
import com.ezen.lolketing.view.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import org.jsoup.Jsoup
import javax.inject.Inject

@AndroidEntryPoint
class NewsActivity : BaseActivity<ActivityNewsBinding>(R.layout.activity_news) {

    @Inject lateinit var auth : FirebaseAuth
    private lateinit var adapter : NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.newsContents.setHasFixedSize(true)
        binding.newsContents.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        getNews()
    }

    private fun setRecyclerView(data: MutableList<NewsItem>) = with(binding) {
        adapter = NewsAdapter(data) {
            startActivity(createIntent(NewsWebViewActivity::class.java).also {
                it.putExtra(Constants.URL, it)
            })
        }
        newsContents.adapter = adapter
    }

    private fun getNews() {
        // todo 검색 api 수정 예정 : https://developers.naver.com/docs/search/news/
        val data : MutableList<NewsItem> = mutableListOf()
        val url = "http://m.inven.co.kr/webzine/wznews.php?site=lol"
        try {
            //여기서 스크래핑 한다.
            val doc = Jsoup.connect(url).get()
            //첫번째 1st_detail_t1의 데이터만 가져오는 듯 하다. 다 가져 올려면 루프문을 돌려야 할듯
            val newsData = doc.select("ul[class=list]").select("li")

            newsData.forEachIndexed { index, element ->
                //실제 페이지의 html 문서의 소스를 보고 어떤 데이터가 어떤 태그를 사용하고 있는지 분석해야 한다.
                val newsTitle = element.select("span[class=title]").text()
                val thumbnail = element.select("span[class=thumb] img").attr("src")
                val info = element.select("span[class=info]").text()
                val newUrl = "http://m.inven.co.kr${element.select("a[class=subject]").attr("href")}"
                data.add(NewsItem(newsTitle, thumbnail, info, newUrl))

                runOnUiThread {
                    setRecyclerView(data)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun logout(view: View) {
        auth.signOut()
        startActivity(LoginActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TOP)
        finish()
    }

    fun moveHome(view: View) {
        startActivity(MainActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TOP)
        finish()
    }
}