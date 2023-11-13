//package com.ezen.lolketing.adapter
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import com.ezen.lolketing.databinding.ItemSliderBinding
//import com.smarteist.autoimageslider.SliderViewAdapter
//
//class EventSliderAdapter (private val images : List<Int>) : SliderViewAdapter<EventSliderAdapter.GuidViewHolder>() {
//
//    class GuidViewHolder(val binding: ItemSliderBinding) : SliderViewAdapter.ViewHolder(binding.root) {
//        fun bind(image : Int) {
//            binding.sliderImg.setImageResource(image)
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup): GuidViewHolder =
//        GuidViewHolder(ItemSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
//
//    override fun getCount(): Int {
//        return images.size
//    }
//
//    override fun onBindViewHolder(viewHolder: GuidViewHolder, position: Int) {
//        viewHolder.bind(images[position])
//    }
//}