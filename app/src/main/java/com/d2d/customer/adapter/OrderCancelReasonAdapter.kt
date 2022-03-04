package com.d2d.customer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.d2d.customer.`interface`.CallbackOrderHistory
import com.d2d.customer.databinding.ItemRowOrderCancelReasonBinding
import com.d2d.customer.model.CancelReasons

class OrderCancelReasonAdapter(val callbackOrderHistory: CallbackOrderHistory):RecyclerView.Adapter<OrderCancelReasonAdapter.MyViewHolder>(){
    var cancelist = mutableListOf<CancelReasons>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater =LayoutInflater.from(parent.context)
        val  binding = ItemRowOrderCancelReasonBinding.inflate(inflater,parent,false)
         return   MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(cancelist[position])
        val radioButton = holder.radioButton
        radioButton.setOnClickListener {
            if (radioButton.isChecked){
                for (data in cancelist){
                    data.isSelected = false
                }
                cancelist[position].isSelected = true
                notifyDataSetChanged()
                callbackOrderHistory.orderCancelReason(cancelist[position].reason)
            }
        }
    }

    override fun getItemCount(): Int {
      return  cancelist.size
    }
    class MyViewHolder(binding: ItemRowOrderCancelReasonBinding):RecyclerView.ViewHolder(binding.root){
        val tvReason =binding.tvCancelReason
        val radioButton = binding.radioButton
      fun bind(data : CancelReasons){
          tvReason.text = data.reason
          radioButton.isChecked = data.isSelected
      }
    }
}