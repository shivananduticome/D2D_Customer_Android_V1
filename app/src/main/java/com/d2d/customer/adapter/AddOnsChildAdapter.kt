package com.d2d.customer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.d2d.customer.R
import com.d2d.customer.`interface`.CallbackSubCategoryDescription
import com.d2d.customer.databinding.ItemRowAddonsBinding
import com.d2d.customer.model.AddOnItem

  open class AddOnsChildAdapter(var addOnsItemListData: MutableList<AddOnItem> = mutableListOf<AddOnItem>(),
                                private val callbackSubCategoryDescription: CallbackSubCategoryDescription
  ):RecyclerView.Adapter<AddOnsChildAdapter.MyViewHolder>() {
      var addOnsItemList = mutableListOf<AddOnItem>()
      private var context:Context? = null
      private var addOnsListData = mutableListOf<AddOnItem>()
      private var list = mutableListOf<AddOnItem>()
      var checkBoxPosition:Int? = null
      private var total: Int = 0
      lateinit var checkBox: CheckBox


      init {
       addOnsItemList = addOnsItemListData
    }

      override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
          context=parent.context
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRowAddonsBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        checkBox = holder.checkBox
        holder.bind(addOnsItemList[position])
            holder.checkBox.setOnClickListener {
            if (holder.checkBox.isChecked) {
                try {
                    checkBoxPosition = holder.adapterPosition
                    addOnsListData.add(addOnsItemList[position])
                    callbackSubCategoryDescription.addOnsList(addOnsListData)
                  //  Toast.makeText(context,addOnsListData.size.toString(),Toast.LENGTH_SHORT).show()
                    total += (addOnsItemList[position].amount)
                }catch (e:Exception){
                }
            }else{
                total -= (addOnsItemList[position].amount)
            }
                callbackSubCategoryDescription.getSelectedAddOnsValue(total.toString())

                if (!holder.checkBox.isChecked){
                    callbackSubCategoryDescription.unCheckRemoveAddons(addOnsItemList[position].addOnId)
                }
            }
    }

    override fun getItemCount(): Int {
        return  addOnsItemList.size
    }

    class MyViewHolder(val binding: ItemRowAddonsBinding):RecyclerView.ViewHolder(binding.root){
        val tvAddOnsName = binding.tvAddOnsName
        val tvAddOnsPrice = binding.tvAddOnsPrice
        val checkBox = binding.checkBox
        val vegOrNonVegImageView = binding.vegOrNonVegImageView
        fun bind(data:AddOnItem){
            tvAddOnsName.text = data.addOnName
            tvAddOnsPrice.text = data.amount.toString()
            if (data.addOnFoodType){
                vegOrNonVegImageView.setBackgroundResource(R.drawable.veg)
            }else{
                vegOrNonVegImageView.setBackgroundResource(R.drawable.non_veg)
            }
        }
    }
}