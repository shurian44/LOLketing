package com.ezen.lolketing.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ItemSliderBinding
import com.ezen.lolketing.view.main.shop.ShopDetailActivity
import com.ezen.lolketing.model.ShopDTO
import com.smarteist.autoimageslider.SliderViewAdapter

class ShopSliderAdapter(
    private val listener : MoveActivityListener,
    private val images : ArrayList<String>,
    private val shops : ArrayList<ShopDTO>?
) : SliderViewAdapter<ShopSliderAdapter.SliderViewHolder>() {

    inner class SliderViewHolder(val binding : ItemSliderBinding) : SliderViewAdapter.ViewHolder(binding.root) {
        fun bind(position : Int) {
            Glide.with(binding.root.context).load(images[position]).into(binding.sliderImg)
            if(shops != null){
                binding.root.setOnClickListener {
                    val intent = Intent(binding.root.context, ShopDetailActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                    intent.putExtra("category", shops[position].group)
                    intent.putExtra("name", shops[position].name)
                    intent.putExtra("image", shops[position].images)
                    intent.putExtra("price", shops[position].price)
                    listener.moveActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup): SliderViewHolder
        = SliderViewHolder(ItemSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(viewHolder: SliderViewHolder, position: Int) {
        viewHolder.bind(position)
    }

    interface MoveActivityListener{
        fun moveActivity(intent : Intent)
    }
}