package com.ezen.lolketing.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ezen.lolketing.view.main.news.NewsWebViewActivity;
import com.ezen.lolketing.R;
import com.ezen.lolketing.model.NewsDTO;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private List<NewsDTO> mDataset;
    setActivityMove listener;

    public static class NewsViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView_title;
        public TextView textView_title, textView_info;

        public NewsViewHolder(@NonNull View v) {
            super(v);
            imageView_title = v.findViewById(R.id.imageView_title);
            textView_title = v.findViewById(R.id.textView_title);
            textView_info = v.findViewById(R.id.textView_info);
        }
    }

    public NewsAdapter(setActivityMove listener){
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
        NewsDTO news = mDataset.get(position);

        String title = news.getTitle();
        if(title != null && title.length() > 0) {
            holder.textView_title.setText(title);
        }else {
            holder.textView_title.setText("-");
        }
        String info = news.getInfo();
        if(info != null && info.length() > 0) {
            holder.textView_info.setText(info);
        }else {
            holder.textView_info.setText("-");
        }


        Glide.with(holder.itemView.getContext()).load(news.getThumbnail()).into(holder.imageView_title);

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

    public void addData(List<NewsDTO> data){
        mDataset = data;
    }
}
