package com.d2d.customer.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View.*
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.d2d.customer.R
import com.d2d.customer.`interface`.CallbackOrderHistory
import com.d2d.customer.databinding.ItemRowOrderHistoryBinding
import com.d2d.customer.model.PlacedOrders

class OrderHistoryAdapter(private val callbackOrderHistory: CallbackOrderHistory) : RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder>() {
    var orderHistoryList = mutableListOf<PlacedOrders>()
    var context:Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
      val inflater = LayoutInflater.from(parent.context)
      val binding = ItemRowOrderHistoryBinding.inflate(inflater,parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        for (data in orderHistoryList){
        }
        holder.bind(orderHistoryList[position],context)
        val btnCancel = holder.binding.tvCancel

           if (orderHistoryList[position].deliveryStatus == "Delivered" ||orderHistoryList[position].deliveryStatus.equals("Cancelled")){
              // btnCancel.visibility = View.GONE
               btnCancel.isEnabled =false
           }else{
               btnCancel.setOnClickListener {
                   callbackOrderHistory.orderCancelDialog()
                   callbackOrderHistory.placedOrderCancel(orderHistoryList[position].userId,orderHistoryList[position].orderId)
               }
           }
        holder.imageViewRightArrow.setOnClickListener {
            callbackOrderHistory.viewOrderHistory(
                orderHistoryList[position].userId,
                orderHistoryList[position].orderId,
                orderHistoryList[position].categoryType,
                orderHistoryList[position].paymentPaid,
                orderHistoryList[position].paymentMethod,
                orderHistoryList[position].address,
                orderHistoryList[position].subscriptionId,
                orderHistoryList[position].orderStatus,
                orderHistoryList[position].startdate,
                orderHistoryList[position].endDate

            )
        }
    }

    override fun getItemCount(): Int {
     return orderHistoryList.size
    }

    class MyViewHolder(val binding: ItemRowOrderHistoryBinding) :RecyclerView.ViewHolder(binding.root){
        val relativeLayout = binding.relativeLayout
        private val tvOrderStatus = binding.tvStatus
        private val tvOrderDate = binding.tvOrderDate
        private val tvPlanEndDate  = binding.tvPlanEndDate
        private val tvOrderNumber = binding.tvOrderNumber
        private val tvCategoryName = binding.tvCategoryName
        private val tvPeriod = binding.tvPeriod
                val tvPrice = binding.tvPrice
        private val tvSubscriptionPlan = binding.tvSubscriptionPlan
                val imageViewRightArrow = binding.imageViewRightArrow
        private val baseLinearLayout = binding.baseLinearLayout

        @SuppressLint("SetTextI18n")
        fun bind(orderHistoryData: PlacedOrders, context: Context?){
            when (orderHistoryData.orderStatus) {
                "Received" -> {
                    tvOrderStatus.setTextColor(context!!.resources.getColor(R.color.green))
                }
                "Cancelled" -> {
                    tvOrderStatus.setTextColor(context!!.resources.getColor(R.color.red))
                }
                "Delivered" -> {
                    tvOrderStatus.setTextColor(context!!.resources.getColor(R.color.d2d_color))
                }
            }


            if (orderHistoryData.categoryType == "Subscription"){
                tvOrderNumber.text = context?.resources?.getString(R.string.order_id)+" "+orderHistoryData.orderId
                tvPrice.text = context?.resources?.getString(R.string.aed) +" "+orderHistoryData.paymentPaid
                tvPeriod.text = orderHistoryData.plan +" "+"Days Plan"
                tvOrderStatus.text = orderHistoryData.orderStatus
                tvCategoryName.text =orderHistoryData.categoryType+" "+"Plan"
                tvSubscriptionPlan.text =":"+" "+orderHistoryData.title
                tvSubscriptionPlan.setTextColor(context?.resources!!.getColor(R.color.d2d_color))
                tvOrderDate.text =context?.resources?.getString(R.string.plan_start_date) +" " +orderHistoryData.orderDate
                tvPlanEndDate.text = context?.resources?.getString(R.string.plan_end_date) +" " +orderHistoryData.endDate
                if (orderHistoryData.orderStatus =="Cancelled"){
                    baseLinearLayout.setBackgroundResource(R.drawable.layout_border_red)
                }else{
                    baseLinearLayout.setBackgroundResource(R.drawable.layout_border_gold)
                }

            }else{
                tvCategoryName.visibility = GONE
                tvPeriod.visibility = GONE
                tvSubscriptionPlan.visibility = GONE
                tvPlanEndDate.visibility = GONE
                tvOrderStatus.text = orderHistoryData.orderStatus
                tvOrderDate.text =context?.resources?.getString(R.string.plan_start_date)+" "+orderHistoryData.orderDate
                tvOrderNumber.text ="Order Number"+orderHistoryData.orderId
                tvPrice.text = context?.resources?.getString(R.string.aed)+" "+orderHistoryData.paymentPaid
            }
            if (orderHistoryData.orderStatus=="Assigned"){
                tvOrderStatus.setTextColor(context!!.resources.getColor(R.color.d2d_color))
                tvOrderStatus.text =context.resources.getString(R.string.out_for_delivery)
            }else if (orderHistoryData.orderStatus =="Prepared" || orderHistoryData.orderStatus=="Packed"){
                tvOrderStatus.setTextColor(context!!.resources.getColor(R.color.green))
                tvOrderStatus.text =context.resources.getString(R.string.received)
            }
        }

    }
}