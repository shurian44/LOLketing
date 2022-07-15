package com.ezen.lolketing.view.login.adater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ezen.lolketing.databinding.ItemAddressBinding
import com.ezen.lolketing.model.SearchAddressResult

class AddressAdapter(
    private val clickListener: (String) -> Unit
) : ListAdapter<SearchAddressResult, AddressViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder =
        AddressViewHolder(
            binding = ItemAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            clickListener = clickListener
        )

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
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