package com.d2d.customer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.d2d.customer.R
import com.d2d.customer.`interface`.CallbackOrderHistory
import com.d2d.customer.databinding.ItemRowViewOrderHistoryBinding
import com.d2d.customer.model.OrderDetail
import com.d2d.customer.model.ViewCartData
import com.squareup.picasso.Picasso

class ViewOrderHistoryAdapter:RecyclerView.Adapter<ViewOrderHistoryAdapter.MyViewHolder>() {
    var viewOrderHistoryList = mutableListOf<OrderDetail>()
    private var context:Context? = null
    private var addOnsName:String? =""
    private var addOnPrice:Int?=0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRowViewOrderHistoryBinding.inflate(inflater,parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        addOnsName = ""
        for (data in viewOrderHistoryList[position].addOnsList){
                addOnsName+= data.addOnName+","+" "
            addOnPrice = addOnPrice?.plus(data.amount)
        }
        holder.bind(viewOrderHistoryList[position],context!!,addOnsName!!,addOnPrice!!)

    }

    override fun getItemCount(): Int {
        return viewOrderHistoryList.size
    }

    class MyViewHolder(val binding: ItemRowViewOrderHistoryBinding):RecyclerView.ViewHolder(binding.root){
         val tvItemName = binding.tvItemName
         val tvPrice = binding.tvPrice
         private val tvItemQuantityTotal = binding.tvItemQuantityTotal
         private val itemQuantity = binding.itemQuantity
         val tvAddOnsName = binding.tvAddOnsName
         val tvAddOnsPrice = binding.tvAddOnsPrice
        val imageView = binding.imageView
        fun bind(data:OrderDetail,context: Context,addOns_Name:String,addOns_price:Int){
            tvItemName.text = data.itemName
            tvPrice.text = context.resources.getString(R.string.aed)+" "+data.itemPrice
            tvItemQuantityTotal.text =context.resources.getString(R.string.aed)+" "+data.itemPrice
            itemQuantity.text = (data.itemBaseQuantity).toString() + " "+"X"
           // val addOnsName = addOns_Name.substring(0, addOns_Name.lastIndexOf("."));
            tvAddOnsName.text = addOns_Name
            tvAddOnsPrice.text =context.resources.getString(R.string.aed)+" "+addOns_price.toString()

            if (data.itemImageUrl == ""||data.equals("")){
               imageView.setBackgroundResource(R.drawable.subcategory)
            }else{
                Picasso.get().load(data.itemImageUrl).into(imageView)
            }

        }
    }
}