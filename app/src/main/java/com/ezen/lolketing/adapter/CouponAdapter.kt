package com.ezen.lolketing.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.R
import com.ezen.lolketing.model.CouponDTO
import com.ezen.lolketing.model.GameDTO
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.item_coupon.view.*

class CouponAdapter (options : FirestoreRecyclerOptions<CouponDTO>)
    : FirestoreRecyclerAdapter<CouponDTO, CouponAdapter.CouponHolder>(options){

    class CouponHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_coupon, parent, false)
        return CouponHolder(view)
    }

    override fun onBindViewHolder(holder: CouponHolder, position: Int, model: CouponDTO) {
        holder.itemView.coupon_title.text = model.title
        holder.itemView.coupon_number.text = model.couponNumber ?: "번호가 없는 쿠폰입니다."
    }

}