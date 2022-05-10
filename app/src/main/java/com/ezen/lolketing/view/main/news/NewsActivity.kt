package com.ezen.lolketing.view.main.news

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.ezen.lolketing.BaseActivity
import com.ezen.lolketing.view.main.MainActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.adapter.NewsAdapter
import com.ezen.lolketing.adapter.NewsAdapter.setActivityMove
import com.ezen.lolketing.databinding.ActivityNewsBinding
import com.ezen.lolketing.model.NewsDTO
import com.ezen.lolketing.view.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import org.jsoup.Jsoup

class NewsActivity : BaseActivity<ActivityNewsBinding>(R.layout.activity_news), setActivityMove {
    private var auth = FirebaseAuth.getInstance()
    var adapter = NewsAdapter(this)
    var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.newsContents.setHasFixedSize(true)
        binding.newsContents.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        queue = Volley.newRequestQueue(this)

        //        queue.add(stringRequest);

        getNews()
    }

    private fun getNews() {
        var data : MutableList<NewsDTO> = mutableListOf()
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
                val url = "http://m.inven.co.kr${element.select("a[class=subject]").attr("href")}"
                data.add(NewsDTO(newsTitle, thumbnail, info, url))

                runOnUiThread {
                    // recyclerView.adapter = MyAdapter(movies)
                    adapter.addData(data)
                    binding.newsContents.adapter = adapter
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun activityMove(intent: Intent) {
        startActivity(intent)
    }

    fun moveHome(view: View) {
        var intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    override fun logout(view: View) {
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP)
        startActivity(intent)
    }
}