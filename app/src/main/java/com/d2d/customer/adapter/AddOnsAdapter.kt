package com.d2d.customer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.d2d.customer.`interface`.CallbackSubCategoryDescription
import com.d2d.customer.databinding.ItemRowAddonsParentBinding
import com.d2d.customer.model.AddOnDetail
import com.d2d.customer.model.AddOnItem


open class AddOnsAdapter(private val callbackSubCategoryDescription: CallbackSubCategoryDescription) :
    RecyclerView.Adapter<AddOnsAdapter.MyViewHolder>(){
    var addOnsList = mutableListOf<AddOnDetail>()
    var context:Context? = null
    var checkBoxPosition:Int? = null
    private var total: Int = 0
    private var radioButtonCheck: Boolean = true
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRowAddonsParentBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.bind(addOnsList[position], holder.adapterPosition, context!!)
             val childMembersAdapter = AddOnsChildAdapter(addOnsList[position].addOnItem as MutableList<AddOnItem>,callbackSubCategoryDescription)
             holder.recyclerView.layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.VERTICAL,false)
              holder.recyclerView.adapter = childMembersAdapter

/*
          if (holder.adapterPosition == 0){
              total += (addOnsList[position].amount)
              callbackSubCategoryDescription.getSelectedAddOnsValue(total.toString())
              dataList.add(addOnsList[position])
              callbackSubCategoryDescription.addOnsList(dataList)
          }

              holder.checkBox.setOnClickListener {
            if(holder.checkBox.isChecked){
                checkBoxPosition = holder.adapterPosition
                total += (addOnsList[position].amount)
            } else {
                total -= (addOnsList[position].amount)
            }
            callbackSubCategoryDescription.getSelectedAddOnsValue(total.toString())
                  dataList.add(addOnsList[position])
                  callbackSubCategoryDescription.addOnsList(dataList)
        }*/


        
    }

    override fun getItemCount(): Int {
        return addOnsList.size
    }

    class MyViewHolder(val binding: ItemRowAddonsParentBinding) : RecyclerView.ViewHolder(binding.root) {
        /* val tvAddOnsName = binding.tvAddOnsName
         val tvAddOnsPrice = binding.tvAddOnsPrice
         val vegOrNonVegImageView = binding.vegOrNonVegImageView
         val checkBox = binding.checkBox

         val tvItemName   = binding.tvItemName
         val  tvType = binding.tvType*/
        val recyclerView = binding.childRecyclerView
         private var addTitle:String? = null
        private var tvType = binding.tvType
        private var tvAddOnTitle = binding.tvAddOnTitle

        fun bind(data: AddOnDetail, position: Int, context: Context) {
           /* tvItemName.text = data.addOnTitle
            tvAddOnsName.text = data.addOnName
            tvAddOnsPrice.text = data.amount.toString()
            if (position == 0) {
                tvType.visibility=View.VISIBLE
                tvType.text = context.resources.getString(R.string.selection)
                checkBox.isChecked = true
                tvItemName.text = data.addOnTitle

            }
           else if (data.type =="addOn"){
                tvType.text = context.resources.getString(R.string.addons)
                tvType.visibility=View.VISIBLE
            }
            tvItemName.text = data.addOnTitle
            tvType.text = data.type*/

            //tvAddOnsPrice.text = data.addOnItem[adapterPosition].amount.toString()

            if (data.type != "" && !data.equals(null)) {
                tvType.visibility = VISIBLE
                tvType.text = data.type.capitalize()
            }
            tvAddOnTitle.text = data.addOnTitle

        }
    }

}


//https://medium.com/nerd-for-tech/nested-recyclerview-in-android-e5afb2b9771a
//https://medium.com/nerd-for-tech/nested-recyclerview-in-android-e5afb2b9771a
