package com.ezen.lolketing

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.ezen.lolketing.adapter.NewsAdapter
import com.ezen.lolketing.adapter.NewsAdapter.setActivityMove
import com.ezen.lolketing.model.NewsDTO
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_news.*
import org.jetbrains.anko.doAsync
import org.jsoup.Jsoup

class NewsActivity : AppCompatActivity(), setActivityMove {
    private var auth = FirebaseAuth.getInstance()
    var adapter = NewsAdapter(this)
    var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        news_contents.setHasFixedSize(true)
        news_contents.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        queue = Volley.newRequestQueue(this)

        //        queue.add(stringRequest);

        getNews()
    }

    private fun getNews() {
        var data : MutableList<NewsDTO> = mutableListOf()
        doAsync {
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
                        news_contents.adapter = adapter
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun logout(view: View?) {
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP)
        startActivity(intent)
    }

    override fun activityMove(intent: Intent) {
        startActivity(intent)
    }

    fun moveHome(view: View) {
        var intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}