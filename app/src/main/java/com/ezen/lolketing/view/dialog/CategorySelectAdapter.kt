package com.ezen.lolketing.view.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.databinding.ItemSelectBinding
import com.ezen.lolketing.model.Category

class CategorySelectAdapter : ListAdapter<Category, CategorySelectAdapter.ViewHolder>(diffUtil) {

    private var beforeSelectIndex = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemSelectBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    inner class ViewHolder(val binding: ItemSelectBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() = with(binding) {
            data = currentList[absoluteAdapterPosition].data
            isSelect = currentList[absoluteAdapterPosition].isSelect

            root.setOnClickListener {
                setOneSelect(absoluteAdapterPosition)
            }
        }
    }

    private fun setOneSelect(position: Int) {
        beforeSelectIndex = currentList.indexOfFirst { it.isSelect }

        if (beforeSelectIndex != -1) {
            currentList[beforeSelectIndex].isSelect = false
            notifyItemChanged(beforeSelectIndex)
        }

        currentList[position].isSelect = true
        notifyItemChanged(position)
    }

    fun setSelectItem(data: String) {
        val selectIndex = currentList.indexOfFirst { it.data == data }
        if(selectIndex == -1) return
        currentList[selectIndex].isSelect = true
        notifyItemChanged(selectIndex)
    }

    fun getSelectInfo() = currentList.firstOrNull { it.isSelect }?.getCode()

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Category>() {
            override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean =
                oldItem.data == newItem.data
        }
    }

}