package com.d2d.customer.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.d2d.customer.R
import com.d2d.customer.`interface`.CallbackSubscription
import com.d2d.customer.databinding.ItemRowSubscriptionPackagesBinding
import com.d2d.customer.model.SubscriptionTitle
import com.d2d.customer.view.AddFragmentToActivity
import com.squareup.picasso.Picasso

class SubscriptionPackageAdapter:RecyclerView.Adapter<SubscriptionPackageAdapter.MyViewHolder>() {
    private var context:Context? = null
    private var callbackSubscription: CallbackSubscription? = null
    var subscriptionTypeList = mutableListOf<SubscriptionTitle>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       context=parent.context
      val inflater = LayoutInflater.from(parent.context)
      val binding = ItemRowSubscriptionPackagesBinding.inflate(inflater,parent,false)
      return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
         holder.bind(subscriptionTypeList[position],context!!)
         holder.binding.relativeLayout.setOnClickListener {
             val sharedPreferences =context?.getSharedPreferences(context?.resources?.getString(R.string.subscription_plan_details_sharedPreferences),Context.MODE_PRIVATE)
             val editor = sharedPreferences?.edit()
             val intent = Intent(context, AddFragmentToActivity::class.java)
                intent.putExtra("FragmentName","SubscriptionDetailsFragment")
                editor?.putString("subscriptionDescription",subscriptionTypeList[position].subscriptionDescription)
                editor?.putString("subscriptionId",subscriptionTypeList[position].subscriptionId)
                editor?.putString("subscriptionImage",subscriptionTypeList[position].subscriptionImage)
                editor?.putString("subscriptionLeastAmount",subscriptionTypeList[position].subscriptionLeastAmount)
                editor?.putString("subscriptionTitle",subscriptionTypeList[position].subscriptionTitle)
                editor?.commit()
             context?.startActivity(intent)
             callbackSubscription?.subscriptionType(subscriptionTypeList[position].subscriptionDescription,subscriptionTypeList[position].subscriptionId,subscriptionTypeList[position].subscriptionImage,
             subscriptionTypeList[position].subscriptionLeastAmount,subscriptionTypeList[position].subscriptionTitle)
        }
    }

    override fun getItemCount(): Int {
        return subscriptionTypeList.size
    }

    class MyViewHolder(val binding: ItemRowSubscriptionPackagesBinding):RecyclerView.ViewHolder(binding.root){
        val tvSubscriptionType = binding.tvSubscriptionType
        val tvDescription = binding.tvDescription
        val tvPrice = binding.tvPrice
        val imageView = binding.imageView
        fun bind(data: SubscriptionTitle,context: Context){
            tvSubscriptionType.text = data.subscriptionTitle
            tvDescription.text = data.subscriptionDescription
            tvPrice.text =context.getString(R.string.aed)+" "+data.subscriptionLeastAmount
            if (data.subscriptionImage ==""||data.subscriptionImage.equals(null)){
               imageView.setBackgroundResource(R.drawable.subcategory)
            }else{
                Picasso.get().load(data.subscriptionImage).into(imageView)
            }
      }
    }
}