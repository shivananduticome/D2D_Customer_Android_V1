package com.d2d.customer.adapter

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.d2d.customer.R
import com.d2d.customer.databinding.ItemRowSubCategoryBinding
import com.d2d.customer.model.SubCategoryData
import com.d2d.customer.view.AddFragmentToActivity
import com.squareup.picasso.Picasso

class SubCategoryAdapter() : RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder>() {
    var subCategoryList = mutableListOf<SubCategoryData>()
    var context:Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder {
        context = parent.context
        var inflater = LayoutInflater.from(parent.context)
        var binding = ItemRowSubCategoryBinding.inflate(inflater,parent,false)
        return MyViewHolder(binding)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       holder.bind(subCategoryList[position])
       holder.binding.subcategoryLinearLayout.setOnClickListener {
           val subCategorysharedPreferences = context?.getSharedPreferences(context?.resources?.getString(
               R.string.subCategory_sharedPreference_data),MODE_PRIVATE)
           val editor =subCategorysharedPreferences?.edit()
           val intent = Intent(context, AddFragmentToActivity::class.java)
              intent.putExtra("FragmentName","SubCategoryDescriptionFragment")
              editor?.putString("mainCategoryId",subCategoryList[position].mainCategoryId)
              editor?.putString("subCategoryId",subCategoryList[position].subCategoryId)
              editor?.putString("subCategoryName",subCategoryList[position].subCategoryName)
              editor?.commit()
           context?.startActivity(intent)
       }
    }

    override fun getItemCount(): Int {
       return subCategoryList.size
    }

    class MyViewHolder(val binding: ItemRowSubCategoryBinding): RecyclerView.ViewHolder(binding.root){
        val tvPrice = binding.tvPrice
        val imageView = binding.imageView
        fun bind(data:SubCategoryData){
            tvPrice.text = data.subCategoryName
            if (data.subCategoryImage =="" || data.subCategoryName.equals(null)){
                imageView.setBackgroundResource(R.drawable.subcategory)
            }else{
                Picasso.get().load(data.subCategoryImage).into(imageView)
            }
        }

    }
}