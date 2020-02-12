package com.ezen.lolketing.adapter;

import android.content.Context;
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
import com.ezen.lolketing.R;
import com.ezen.lolketing.model.NewsData;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private List<NewsData> mDataset;
    private static View.OnClickListener onClickListener;

    public static class NewsViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView_title;
        public TextView textView_title, textView_date, textView_author;
        public View rootView;

        public NewsViewHolder(@NonNull View v) {
            super(v);
            imageView_title = v.findViewById(R.id.imageView_title);
            textView_title = v.findViewById(R.id.textView_title);
            textView_date = v.findViewById(R.id.textView_date);
            textView_author = v.findViewById(R.id.textView_author);
            rootView = v;
            v.setClickable(true);
            v.setEnabled(true);
            v.setOnClickListener(onClickListener);
        }
    }

    public NewsAdapter(List<NewsData> myDataset, Context context, View.OnClickListener onClick) {
        mDataset = myDataset;
        onClickListener = onClick;
    }
    @Override
    public NewsAdapter.NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
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

        // tag - label
        holder.rootView.setTag(position);

    }

    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }

    public NewsData getNews(int position) {
        return mDataset != null ? mDataset.get(position) : null;
    }
}
