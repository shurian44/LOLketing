package com.ezen.lolketing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ezen.lolketing.adapter.NewsAdapter;
import com.ezen.lolketing.model.NewsData;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        recyclerView = findViewById(R.id.news_contents);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        queue = Volley.newRequestQueue(this);
        getNews();

        recyclerView.setAdapter(mAdapter);

//        queue.add(stringRequest);
    }

    public void getNews() {
        String url = "https://newsapi.org/v2/top-headlines?country=kr&apiKey=159395d777d845aeb527f35abb7abcb3";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            JSONArray arrayArticles = jsonObj.getJSONArray("articles");

                            // response -> NewsData Class로 분류.
                            List<NewsData> news = new ArrayList<>();

                            for(int i = 0, j = arrayArticles.length(); i < j; i++) {
                                JSONObject obj = arrayArticles.getJSONObject(i);
                                NewsData newsData = new NewsData();
                                newsData.setAuthor(obj.getString("author"));
                                newsData.setTitle( obj.getString("title"));
                                newsData.setUrl(obj.getString("url"));
                                newsData.setUrlToImage(obj.getString("urlToImage"));
                                newsData.setPublishedAt(obj.getString("publishedAt"));
                                news.add(newsData);

                            }

                            // specify an adapter (see also next example)
                            mAdapter = new NewsAdapter(news, NewsActivity.this, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Object obj = v.getTag();
                                    if(obj != null) {
                                        int position = (int)obj;
                                        Intent intent = new Intent();
                                        startActivity(intent);
                                    }
                                }
                            });

                            recyclerView.setAdapter(mAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void logout(View view) {
        auth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|
                        Intent.FLAG_ACTIVITY_CLEAR_TOP|
                        Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);

        startActivity(intent);
    }


}
