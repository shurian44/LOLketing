package com.ezen.lolketing.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.ezen.lolketing.R
import com.smarteist.autoimageslider.SliderViewAdapter
import kotlinx.android.synthetic.main.item_slider.view.*

class SliderAdapter(context: Context, images : List<Int>) : SliderViewAdapter<SliderAdapter.SliderViewHolder>() {
    var images = images

    class SliderViewHolder(itemView: View) : SliderViewAdapter.ViewHolder(itemView){
        var slider_img = itemView.findViewById<ImageView>(R.id.slider_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup?): SliderViewHolder {
        var view = LayoutInflater.from(parent?.context).inflate(R.layout.item_slider, parent, false)
        return SliderViewHolder(view)
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(viewHolder: SliderViewHolder?, position: Int) {
        viewHolder?.itemView!!.slider_img.setImageResource(images[position])

    }
}