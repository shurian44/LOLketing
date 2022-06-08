package com.ezen.lolketing.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ItemNewsBinding
import com.ezen.lolketing.model.NewsContents

class NewsAdapter(
    private val listener : (String) -> Unit
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private val list = mutableListOf<NewsContents>()

    inner class NewsViewHolder(private val binding : ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(news : NewsContents) = with(binding) {
            newsContents = news
            if (adapterPosition % 2 == 0) {
                root.setBackgroundResource(R.color.black)
            } else {
                root.setBackgroundResource(R.color.light_black)
            }
            root.setOnClickListener {
                listener(news.url)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder =
        NewsViewHolder(ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun addNewsItems(list: List<NewsContents>) {
        list.forEach {
            this.list.add(it)
            notifyItemChanged(list.lastIndex)
        }
    }

}