package com.ezen.lolketing.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ezen.lolketing.R
import com.ezen.lolketing.ShopDetailActivity
import com.ezen.lolketing.model.ShopDTO
import com.smarteist.autoimageslider.SliderViewAdapter
import kotlinx.android.synthetic.main.item_slider.view.*

class SliderAdapter(context: Context, listener : SliderAdapter.MoveActivityListener, images : List<Int>, shops : List<ShopDTO>?) : SliderViewAdapter<SliderAdapter.SliderViewHolder>() {
    private var listener = listener
    var images = images
    var shops = shops ?: null

    class SliderViewHolder(itemView: View) : SliderViewAdapter.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup?): SliderViewHolder {
        var view = LayoutInflater.from(parent?.context).inflate(R.layout.item_slider, parent, false)
        return SliderViewHolder(view)
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(viewHolder: SliderViewHolder?, position: Int) {
        viewHolder?.itemView!!.slider_img.setImageResource(images[position])

        if(shops != null){
            viewHolder.itemView.setOnClickListener {
                val intent = Intent(viewHolder.itemView.context, ShopDetailActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                intent.putExtra("category", shops?.get(position)?.group)
                intent.putExtra("name", shops?.get(position)?.name)
                intent.putExtra("image", shops?.get(position)?.images)
                intent.putExtra("price", shops?.get(position)?.price)
                listener.moveActivity(intent)
            }
        }
    }

    interface MoveActivityListener{
        fun moveActivity(intent : Intent)
    }
}