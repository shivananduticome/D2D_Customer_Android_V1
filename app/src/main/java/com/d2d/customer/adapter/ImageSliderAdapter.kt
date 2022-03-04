package com.d2d.customer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.d2d.customer.R
import com.smarteist.autoimageslider.SliderViewAdapter
import com.squareup.picasso.Picasso



class ImageSliderAdapter() :
    SliderViewAdapter<ImageSliderAdapter.VH>() {
    private var context:Context? = null
    private var mSliderItems = ArrayList<String>()
    fun renewItems(sliderItems: ArrayList<String>) {
        mSliderItems = sliderItems
        notifyDataSetChanged()
    }

    fun addItem(sliderItem: String) {
        mSliderItems.add(sliderItem)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup): VH {
        context = parent.context
        val inflate: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_slider, null)
        return VH(inflate)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        //load image into view
        Picasso.get().load(mSliderItems[position]).fit().into(holder.imageView)
            holder.imageView.setOnClickListener {
               // Toast.makeText(context,mSliderItems[position],Toast.LENGTH_SHORT).show()
            }
    }

    override fun getCount(): Int {
        return mSliderItems.size
    }

    inner class VH(itemView: View) : ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.imageSlider)
    }
}

//https://devendrac706.medium.com/android-image-slider-with-indicator-auto-image-slider-in-android-studio-kotlin-kotlin-tutorial-ed55e79a0cad
//https://site-valley.com/image-slider-in-android-using-kotlin/