package com.ezen.lolketing.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ezen.lolketing.NewsWebViewActivity;
import com.ezen.lolketing.R;
import com.ezen.lolketing.model.NewsData;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private List<NewsData> mDataset;
    setActivityMove listener;

    public static class NewsViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView_title;
        public TextView textView_title, textView_date, textView_author;

        public NewsViewHolder(@NonNull View v) {
            super(v);
            imageView_title = v.findViewById(R.id.imageView_title);
            textView_title = v.findViewById(R.id.textView_title);
            textView_date = v.findViewById(R.id.textView_date);
            textView_author = v.findViewById(R.id.textView_author);
        }
    }

    public NewsAdapter(List<NewsData> myDataset, setActivityMove listener){
        this.mDataset = myDataset;
        this.listener = listener;
    }
    @Override
    public NewsAdapter.NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsViewHolder holder, final int position) {
        NewsData news = mDataset.get(position);

        String author = news.getAuthor();
        if(author != null && author.length() > 0) {
            holder.textView_author.setText(author);
        }else {
            holder.textView_author.setText("-");
        }

        String title = news.getTitle();
        if(title != null && title.length() > 0) {
            holder.textView_title.setText(title);
        }else {
            holder.textView_title.setText("-");
        }

        String date = news.getPublishedAt();
        if(date != null && date.length() > 0) {
            holder.textView_date.setText(date);
        }else {
            holder.textView_date.setText("-");
        }

        Glide.with(holder.itemView.getContext()).load(news.getUrlToImage()).into(holder.imageView_title);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), NewsWebViewActivity.class);
                intent.putExtra("url", mDataset.get(position).getUrl());
                listener.activityMove(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }

    public interface setActivityMove{
        void activityMove(Intent intent);
    }
}
