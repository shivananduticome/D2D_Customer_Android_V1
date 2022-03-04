package com.d2d.customer.view.fragment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.d2d.customer.R
import com.d2d.customer.`interface`.CallbackAddress
import com.d2d.customer.adapter.AddressAdapter
import com.d2d.customer.databinding.AddressFragmentBinding
import com.d2d.customer.model.CommonResponse
import com.d2d.customer.model.ManageAddressDetailsResponseModel
import com.d2d.customer.util.LocationPermissionUtil
import com.d2d.customer.util.ProgressDialog
import com.d2d.customer.view.AddFragmentToActivity
import com.d2d.customer.viewmodel.AddressViewModel

class AddressFragment : Fragment(),CallbackAddress {
    private lateinit var binding: AddressFragmentBinding
    private lateinit var addressAdapter: AddressAdapter
    private lateinit var registrationSharedPreferences:SharedPreferences
    private var userId:String? = null
    private lateinit var progressDialog:Dialog

    companion object {
        fun newInstance() = AddressFragment()
    }

    private lateinit var viewModel: AddressViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = AddressFragmentBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this)[AddressViewModel::class.java]
        registrationSharedPreferences = (activity as AppCompatActivity).getSharedPreferences(resources.getString(R.string.registration_details_sharedPreferences), Context.MODE_PRIVATE)
        userId = registrationSharedPreferences.getString("userId","")
        progressDialog = ProgressDialog.progressDialog(activity as AppCompatActivity)
        val view:View = binding.root
        initAndClickEvent()
        fetchAddressList(userId!!)
        LocationPermissionUtil.checkLocationPermissions(activity as AppCompatActivity, this::onLocationPermissionsGranted)
        if (userId =="" ||userId.equals(null)){
            Toast.makeText(context,"Please Login",Toast.LENGTH_SHORT).show()
            activity?.finish()
        }
        return view
    }

    private fun initAndClickEvent(){
        /*Recyclerview Initialization*/
       val recyclerView = binding.recyclerView
           recyclerView.layoutManager = LinearLayoutManager(activity)
           recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
           addressAdapter = AddressAdapter(this)
           recyclerView.adapter = addressAdapter
           binding.backPressImageView.setOnClickListener {
               activity?.finish()
           }

      binding.floatingButtonAddAddress.setOnClickListener {
          val intent = Intent(context,AddFragmentToActivity::class.java)
          intent.putExtra("FragmentName","GoogleMapFragment")
          startActivity(intent)
          activity?.finish()
      }/*floating point button click event*/
    }

    override fun onResume() {
        super.onResume()
        fetchAddressList(userId!!)
    }

    private fun fetchAddressList(userId:String){
        progressDialog.show()
        viewModel.addressListObservable().observe(viewLifecycleOwner, Observer<ManageAddressDetailsResponseModel> {
          if (it.statusCode == 400){
            Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
              progressDialog.dismiss()
              addressAdapter.notifyDataSetChanged()
              addressAdapter.addressList.clear()

          }else{
              progressDialog.dismiss()
            //  Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
              addressAdapter.addressList = it.AdressDetails.toMutableList()
              addressAdapter.notifyDataSetChanged()
          }
        })
        viewModel.apiCallAddressList(userId)
    }

    override fun deleteAddress(userDetailsId: String) {
        progressDialog.show()
        viewModel.deleteAddressObservable().observe(viewLifecycleOwner, Observer<CommonResponse> {
         if (it.statusCode == 400) {
             progressDialog.dismiss()
             Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
         }else{
             progressDialog.dismiss()
            // Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
             fetchAddressList(userId!!)
         }
        })
        viewModel.apiCallDeleteAddress(userDetailsId)
    }/*delete Address*/

    override fun changesAddress(userId: String, objectId: String) {
        progressDialog.show()
      viewModel.changeAddressObservable().observe(viewLifecycleOwner, Observer<CommonResponse> {
          if (it.statusCode ==400){
              Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
              progressDialog.dismiss()
          }else{
             // Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
              fetchAddressList(userId)
              progressDialog.dismiss()
          }
      })
      viewModel.apiCallForChangeAddress(userId,objectId)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        LocationPermissionUtil.onRequestPermissionsResult(
            activity as AppCompatActivity,
            requestCode,
            this::onLocationPermissionsGranted
        )
    }
    private fun onLocationPermissionsGranted() {
        Toast.makeText(context, "Background location permitted, starting location tracking...", Toast.LENGTH_LONG).show()
    }
}

//https://code.luasoftware.com/tutorials/android/android-request-location-permission/
//https://proandroiddev.com/easy-way-to-ask-for-permissions-on-android-62a9ae4a22b0