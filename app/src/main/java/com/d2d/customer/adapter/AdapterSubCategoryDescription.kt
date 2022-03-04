package com.d2d.customer.adapter

import android.content.Context
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.d2d.customer.R
import com.d2d.customer.`interface`.CallbackSubCategoryDescription
import com.d2d.customer.databinding.ItemRowSubcategoryDescriptionBinding
import com.d2d.customer.model.SubCategoryMenuData
import com.squareup.picasso.Picasso
import java.lang.Exception

class AdapterSubCategoryDescription(var callbackSubCategoryDescription: CallbackSubCategoryDescription) :RecyclerView.Adapter<AdapterSubCategoryDescription.MyViewHolder>(){
     var menuList = mutableListOf<SubCategoryMenuData>()
     var context:Context? = null
     var userId:String? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
           context=parent.context
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRowSubcategoryDescriptionBinding.inflate(inflater,parent,false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
         holder.bind(menuList[position],context!!)
         holder.binding.relativeLayout.setOnClickListener {
             val registrationSharedPreferences = context?.getSharedPreferences(context?.resources?.getString(R.string.registration_details_sharedPreferences), Context.MODE_PRIVATE)
                   userId = registrationSharedPreferences?.getString("userId", "")
             if (userId.equals("") || userId.equals(null)) {
                // Toast.makeText(context, "Before Add to Cart Please Register!!", Toast.LENGTH_LONG).show()
                 /*val intent = Intent(context, RegistrationLoginVerifyOtpActivity::class.java)
                 context?.startActivity(intent)*/
                 callbackSubCategoryDescription.userUserRegistration()
             }
             else if ((menuList[position].itemQuantity).toInt()<=0) {
                 Toast.makeText(context, "Out of Stock!!", Toast.LENGTH_LONG).show()
             }
             else{
                 callbackSubCategoryDescription.addToCartData(menuList[position].itemName, menuList[position].itemDescription,menuList[position].itemPrice,
                     menuList[position].itemMainCategoryName,menuList[position].itemSubCategoryName, menuList[position].itemFoodType,menuList[position].itemBaseQuantity,
                     menuList[position].itemId,menuList[position].itemImageUrl)
             }
         }

        holder.binding.addOnsButton.setOnClickListener {
          /*  callbackSubCategoryDescription.addOnsButtonClickEvent(menuList[position].itemName, menuList[position].itemDescription,menuList[position].itemPrice,
                menuList[position].itemMainCategoryName,menuList[position].itemSubCategoryName, menuList[position].itemFoodType,menuList[position].itemBaseQuantity,
                menuList[position].itemId,menuList[position].itemImageUrl)*/
            val sharedPreferences = context?.getSharedPreferences(context?.resources?.getString(R.string.registration_details_sharedPreferences), Context.MODE_PRIVATE)
            userId = sharedPreferences?.getString("userId", "")
            if (userId.equals("") || userId.equals(null)) {
                Toast.makeText(context, "Before Add to Cart Please Register!!", Toast.LENGTH_LONG).show()
               /* val intent = Intent(context, RegistrationLoginVerifyOtpActivity::class.java)
                context?.startActivity(intent)*/
                 callbackSubCategoryDescription.userUserRegistration()
            }else if ((menuList[position].itemQuantity).toInt()<=0) {
                Toast.makeText(context, "Out of Stock!!", Toast.LENGTH_LONG).show()
            }else{
                callbackSubCategoryDescription.addToCartData(menuList[position].itemName, menuList[position].itemDescription,menuList[position].itemPrice,
                    menuList[position].itemMainCategoryName,menuList[position].itemSubCategoryName, menuList[position].itemFoodType,menuList[position].itemBaseQuantity,
                    menuList[position].itemId,menuList[position].itemImageUrl)
            }
        }


    }


    override fun getItemCount(): Int {
        return menuList.size
    }

    class MyViewHolder(val binding: ItemRowSubcategoryDescriptionBinding):RecyclerView.ViewHolder(binding.root){
        val tvTitle = binding.tvItemName
        val tvDescription =binding.tvDescription
        val tvPrice = binding.tvPrice
        val vegNonVegImageview = binding.vegOrNonVegImageView
        val mainCourseImageView = binding.mainCourseImageView

        fun bind(data:SubCategoryMenuData,context: Context){
            try {
                tvTitle.text = data.itemName
                tvDescription.text = data.itemDescription
                tvPrice.text = "AED"+" "+data.itemPrice
                if (data.itemFoodType) {
                    vegNonVegImageview.setBackgroundResource(R.drawable.veg)
                }else{
                    vegNonVegImageview.setBackgroundResource(R.drawable.non_veg)
                }

                if (data.itemImageUrl =="" || data.itemImageUrl == null){
                    mainCourseImageView.setBackgroundResource(R.drawable.subcategory)
                }else{
                    Picasso.get().load(data.itemImageUrl).into(mainCourseImageView)
                }
            }catch (e:Exception){
              //  Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show()
            }
       }

    }
}
