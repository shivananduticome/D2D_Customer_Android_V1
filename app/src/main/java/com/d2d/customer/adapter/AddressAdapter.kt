package com.d2d.customer.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.d2d.customer.R
import com.d2d.customer.`interface`.CallbackAddress
import com.d2d.customer.databinding.ItemRowAddressBinding
import com.d2d.customer.model.AdressDetail

class AddressAdapter(val callbackAddress: CallbackAddress):RecyclerView.Adapter<AddressAdapter.MyViewHolder>() {
     var addressList= mutableListOf<AdressDetail>()
     var contex:Context?= null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        contex = parent.context
      val inflater =LayoutInflater.from(parent.context)
      val binding = ItemRowAddressBinding.inflate(inflater,parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(addressList[position])
        holder.binding.deleteImageView.setOnClickListener {
                callbackAddress.deleteAddress(addressList[position].userDetailOBJId)
        }

        holder.binding.radioButton.setOnClickListener {
           callbackAddress.changesAddress(addressList[position].userId,addressList[position].userDetailOBJId)
        }
    }

    override fun getItemCount(): Int {
      return addressList.size
    }
    class MyViewHolder(val binding: ItemRowAddressBinding):RecyclerView.ViewHolder(binding.root){
        private var tvPlace = binding.tvPlace
        private var tvLocation =binding.tvLocation
        private var radioButton = binding.radioButton
        private var placeImageView =binding.placeImageView
        private var deleteImageView = binding.deleteImageView
        fun bind(data:AdressDetail){
           tvPlace.text =data.saveAddressAs
            tvLocation.text =data.address
            radioButton.isChecked = data.status
            if (data.saveAddressAs=="Home"){
               placeImageView.setBackgroundResource(R.drawable.ic_home_black)
            }else if (data.saveAddressAs=="Work"){
                placeImageView.setBackgroundResource(R.drawable.ic_work)
            }else{
                placeImageView.setBackgroundResource(R.drawable.ic_other_houses)

            }
            if (data.status){
                deleteImageView.visibility = GONE
            }else{
                deleteImageView.visibility = VISIBLE

            }
        }

    }
}