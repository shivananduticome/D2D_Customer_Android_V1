package com.d2d.customer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.d2d.customer.model.AddressResponseModel
import com.d2d.customer.model.CommonResponse
import com.d2d.customer.model.OrderCancelReasonResponseModel
import com.d2d.customer.model.SectorDetailsResponse
import com.d2d.customer.retrofit.ApiService
import com.d2d.customer.retrofit.RetroInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GoogleMapViewModel : ViewModel() {
    private var addressResponse: MutableLiveData<AddressResponseModel>
    private var sectorResponse: MutableLiveData<SectorDetailsResponse>


    init {
        addressResponse = MutableLiveData()
        sectorResponse = MutableLiveData()
    }

    /*get the Main Category list of data*/
    fun addressObservable():MutableLiveData<AddressResponseModel>{
        return addressResponse
    }

    fun apiCallAddress(userId:String,latLong:String,address:String,sectorId:String,houseOrFloorNo:String,landMark:String,place:String){
        val retroInstance = RetroInstance.getRetroInstance().create(ApiService::class.java)
        val call = retroInstance.address(userId,latLong,address,sectorId,houseOrFloorNo,landMark,place)
        call.enqueue(object : Callback<AddressResponseModel> {
            override fun onResponse(call: Call<AddressResponseModel>, response: Response<AddressResponseModel>) {
                if (response.isSuccessful){
                    addressResponse.postValue(response.body())
                }
            }
            override fun onFailure(call: Call<AddressResponseModel>, t: Throwable) {
                // mainCategoryResponse.postValue(null)
            }
        })
    }


    /*Order Cancel Reason*/
    fun sectorObservable():MutableLiveData<SectorDetailsResponse>{
        return sectorResponse
    }
    fun apiCallForSectorDetails(){
        val retroInstance = RetroInstance.getRetroInstance().create(ApiService::class.java)
        val call = retroInstance.sectorDetails()
        call.enqueue(object : Callback<SectorDetailsResponse>{
            override fun onResponse(call: Call<SectorDetailsResponse>, response: Response<SectorDetailsResponse>) {
                if (response.isSuccessful){
                    sectorResponse.postValue(response.body())
                }
            }
            override fun onFailure(call: Call<SectorDetailsResponse>, t: Throwable) {
                //orderHistoryResponse.postValue(null)
            }

        })
    }

}