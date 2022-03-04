package com.d2d.customer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.d2d.customer.R
import com.d2d.customer.databinding.ItemRowUpcomingMealsBinding
import com.d2d.customer.model.upcomingMeals
import com.squareup.picasso.Picasso


class UpcomingMealsAdapter:RecyclerView.Adapter<UpcomingMealsAdapter.MyViewHolder>() {
    var upcomingMealsList = mutableListOf<upcomingMeals>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRowUpcomingMealsBinding.inflate(inflater,parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(upcomingMealsList[position])
    }

    override fun getItemCount(): Int {
     return  upcomingMealsList.size
    }

    class MyViewHolder(val binding: ItemRowUpcomingMealsBinding):RecyclerView.ViewHolder(binding.root){
        val tvItemName = binding.tvItemName
        val tvDay = binding.tvDay
        val tvDescription = binding.tvDescription
        val imageView = binding.imageView

      fun bind(data:upcomingMeals){
        tvItemName.text = data.meal
        tvDay.text = data.day
        tvDescription.text = data.description
        if (data.image == "" || data.equals(null)){
           imageView.setBackgroundResource(R.drawable.subcategory)
        }else{
            Picasso.get().load(data.image).into(imageView)
        }

      }
    }
}