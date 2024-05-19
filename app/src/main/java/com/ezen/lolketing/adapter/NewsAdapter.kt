package com.ezen.lolketing.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ItemNewsBinding
import com.ezen.network.model.NewsContents

class NewsAdapter(
    private val onClick: (String) -> Unit
) : ListAdapter<NewsContents, NewsViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder =
        NewsViewHolder(
            ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(currentList[position], onClick)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<NewsContents>() {
            override fun areItemsTheSame(oldItem: NewsContents, newItem: NewsContents) =
                oldItem.url == newItem.url

            override fun areContentsTheSame(oldItem: NewsContents, newItem: NewsContents) =
                oldItem == newItem

        }
    }
}

class NewsViewHolder(
    private val binding: ItemNewsBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        news: NewsContents,
        onClick: (String) -> Unit
    ) = with(binding) {
        newsContents = news

        root.setBackgroundResource(
            if (absoluteAdapterPosition % 2 == 0) R.color.black else R.color.light_black
        )

        root.setOnClickListener {
            onClick(news.url)
        }
    }
}