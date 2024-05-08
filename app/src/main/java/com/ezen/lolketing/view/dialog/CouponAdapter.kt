package com.ezen.lolketing.view.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.databinding.ItemCouponBinding
import com.ezen.network.model.Coupon

class CouponAdapter(
    private val onClick: (Int) -> Unit
): ListAdapter<Coupon, CouponViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CouponViewHolder(
            ItemCouponBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: CouponViewHolder, position: Int) {
        holder.bind(
            coupon = currentList[position],
            onClick = onClick
        )
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Coupon>() {
            override fun areItemsTheSame(oldItem: Coupon, newItem: Coupon) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Coupon, newItem: Coupon) =
                oldItem == newItem
        }
    }
}

class CouponViewHolder(
    private val binding: ItemCouponBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(
        coupon: Coupon,
        onClick: (Int) -> Unit
    ) = with(binding) {
        this.coupon = coupon
        btnReceive.setOnClickListener { onClick(coupon.id) }
    }
}