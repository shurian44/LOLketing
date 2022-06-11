package com.ezen.lolketing.view.login.adater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.databinding.ItemAddressBinding
import com.ezen.lolketing.model.SearchAddressResult

class AddressAdapter(
    val clickListener: (String) -> Unit
) : ListAdapter<SearchAddressResult, AddressAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(val binding: ItemAddressBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: SearchAddressResult) = with(binding) {
            txtAddress.text = data.roadAddr
            root.setOnClickListener {
                clickListener(data.roadAddr ?: "")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<SearchAddressResult>() {
            override fun areItemsTheSame(
                oldItem: SearchAddressResult,
                newItem: SearchAddressResult
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: SearchAddressResult,
                newItem: SearchAddressResult
            ): Boolean = oldItem.roadAddr == newItem.roadAddr
        }
    }

}