package com.ezen.lolketing.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ezen.lolketing.R
import com.smarteist.autoimageslider.SliderViewAdapter
import kotlinx.android.synthetic.main.item_slider.view.*

class EventSliderAdapter (images : List<Int>) : SliderViewAdapter<EventSliderAdapter.GuidViewHolder>() {
    var images = images

    class GuidViewHolder(itemView: View) : SliderViewAdapter.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup?): GuidViewHolder {
        var view = LayoutInflater.from(parent?.context).inflate(R.layout.item_slider, parent, false)
        return GuidViewHolder(view)
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(viewHolder: GuidViewHolder?, position: Int) {
        viewHolder?.itemView?.slider_img?.setImageResource(images[position])
    }
}