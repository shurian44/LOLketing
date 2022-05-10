package com.ezen.lolketing.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.databinding.ItemCouponBinding
import com.ezen.lolketing.model.CouponDTO
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class CouponAdapter (options : FirestoreRecyclerOptions<CouponDTO>)
    : FirestoreRecyclerAdapter<CouponDTO, CouponAdapter.CouponHolder>(options){

    inner class CouponHolder(private val binding: ItemCouponBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: CouponDTO) {
            binding.couponTitle.text = model.title
            binding.couponNumber.text = model.couponNumber ?: "번호가 없는 쿠폰입니다."
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponHolder =
        CouponHolder(ItemCouponBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: CouponHolder, position: Int, model: CouponDTO) {
        holder.bind(model)
    }

}