package com.ezen.lolketing.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.databinding.ItemNewsBinding
import com.ezen.lolketing.model.NewsItem
import com.ezen.lolketing.util.setGlide

class NewsAdapter(
    private var mDataset: List<NewsItem>? = null,
    private val listener : (String?) -> Unit
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {


    inner class NewsViewHolder(private val binding : ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(news : NewsItem) = with(binding) {

            textViewTitle.text = if (news.title.isNullOrEmpty()) {
                "-"
            } else {
                news.title
            }

            textViewInfo.text = if (news.info.isNullOrEmpty()) {
                "-"
            } else {
                news.info
            }

            news.thumbnail?.let{
                setGlide(imageViewTitle, it)
            }

            binding.root.setOnClickListener {
                listener(news.url)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder =
        NewsViewHolder(ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        mDataset?.let {
            holder.bind(it[position])
        }
    }

    override fun getItemCount(): Int = if (mDataset == null) 0 else mDataset!!.size

}