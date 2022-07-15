package com.ezen.lolketing.view.login.adater

import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.databinding.ItemAddressBinding
import com.ezen.lolketing.model.SearchAddressResult

class AddressViewHolder(val binding: ItemAddressBinding, val clickListener: (String) -> Unit) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: SearchAddressResult) = with(binding) {
        txtAddress.text = data.roadAddr
        root.setOnClickListener {
            clickListener(data.roadAddr ?: "")
        }
    }
}