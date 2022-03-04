package com.d2d.customer.adapter

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.d2d.customer.R
import com.d2d.customer.`interface`.CallbackViewCart
import com.d2d.customer.databinding.ItemRowCartBinding
import com.d2d.customer.model.AddOnSelections
import com.d2d.customer.model.ViewCartData
import com.d2d.customer.model.addOnSelection
import com.d2d.customer.view.DashboardActivity
import com.squareup.picasso.Picasso
import java.lang.Exception

class CartAdapter(val callbackViewCart: CallbackViewCart): RecyclerView.Adapter<CartAdapter.MyViewHolder>() {
    var addToCartList = mutableListOf<ViewCartData>()
    private var context: Context? = null
    private var addOnsName:String? =""
    private var addOnsList = mutableListOf<addOnSelection>()
    private var tempAddOnsList = mutableListOf<addOnSelection>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       context = parent.context
       val inflater = LayoutInflater.from(parent.context)
       val binding = ItemRowCartBinding.inflate(inflater,parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        try {
            addOnsList.clear()
            addOnsName=""
            for(item in addToCartList[position].addOnSelection){
                addOnsName+= item.addOnName+","+" "
            }
            holder.bind(addToCartList[position],addOnsName!!,context!!)

            for (data in addToCartList[position].addOnSelection){
                addOnsList.addAll(listOf(data))
            }
            callbackViewCart.passPlaceOrderList(addToCartList,addToCartList[position].addOnSelection as MutableList<addOnSelection>)
            //Log.e("DashboardActivity", addToCartList[position].addOnSelection.toString())
            // Toast.makeText(context,(addToCartList[position].addOnSelection as MutableList<addOnSelection>).size.toString(),Toast.LENGTH_SHORT).show()
            holder.binding.tvDeleteItem.setOnClickListener {
                callbackViewCart.deleteItem(addToCartList[position].userId,addToCartList[position].cartId)
            }

            /*Quantity Decrement*/
            holder.binding.tvDecrement.setOnClickListener {
                val itemBaseQuantity = addToCartList[position].itemBaseQuantity.toInt()
                if(itemBaseQuantity==1){
                    callbackViewCart.deleteItem(addToCartList[position].userId,addToCartList[position].cartId)
                }else{
                    addToCartList[position].itemBaseQuantity = (itemBaseQuantity - 1)
                    callbackViewCart.decrementOrIncremenItem(addToCartList[position].userId,addToCartList[position].cartId,addToCartList[position].itemBaseQuantity,addToCartList[position].itemPrice)
                    notifyItemChanged(position)
                }


                //callbackViewCart.passPlaceOrderList(addToCartList)
                //Toast.makeText(context, "This option under developing..", Toast.LENGTH_LONG).show()

            }
            /*Quantity Decrement*/
            holder.binding.tvIncrement.setOnClickListener {
                val itemBaseQuantity = addToCartList[position].itemBaseQuantity.toInt()
                addToCartList[position].itemBaseQuantity = (itemBaseQuantity + 1)
                callbackViewCart.decrementOrIncremenItem(addToCartList[position].userId,addToCartList[position].cartId,addToCartList[position].itemBaseQuantity,addToCartList[position].itemPrice)
                notifyItemChanged(position)

            }

        }catch (e:Exception){
            Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return addToCartList.size

    }

    class MyViewHolder(val binding: ItemRowCartBinding): RecyclerView.ViewHolder(binding.root){
        val imageView = binding.imageView
        val tvTitle = binding.tvItemName
        val tvPrice = binding.tvPrice
        val tvItemBaseQuantity =binding.tvItemBaseQuantity
        val vegOrNonVegImageView = binding.vegOrNonVegImageView
        val tvAddOnsName = binding.tvAddOnsName
        fun bind(data: ViewCartData,addOns_name:String,context: Context)
           {
              try {
                  tvTitle.text = data.itemName
                  tvPrice.text = "AED"+" "+ data.itemPrice
                  tvItemBaseQuantity.text = data.itemBaseQuantity.toString()
                  tvAddOnsName.text = addOns_name
                  if (data.itemFoodType) {
                      vegOrNonVegImageView.setBackgroundResource(R.drawable.veg)
                  }else{
                      vegOrNonVegImageView.setBackgroundResource(R.drawable.non_veg)
                  }

                  if (data.itemImageUrl == "" || data.itemImageUrl.equals(null)){
                      imageView.setBackgroundResource(R.drawable.subcategory)
                  }else{
                      Picasso.get().load(data.itemImageUrl).into(imageView)
                  }
              }catch (e:Exception){
                  Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show()
              }
        }

    }
}

