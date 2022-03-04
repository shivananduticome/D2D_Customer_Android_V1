package com.d2d.customer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.d2d.customer.model.CommonResponse
import com.d2d.customer.model.ManageAddressDetailsResponseModel
import com.d2d.customer.retrofit.ApiService
import com.d2d.customer.retrofit.RetroInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddressViewModel : ViewModel() {
    private var addressListResponse: MutableLiveData<ManageAddressDetailsResponseModel> = MutableLiveData()
    private var deleteResponse: MutableLiveData<CommonResponse> = MutableLiveData()
    private var changeAddressResponse:MutableLiveData<CommonResponse> =MutableLiveData()

    /*get the Main Category list of data*/
    fun addressListObservable(): MutableLiveData<ManageAddressDetailsResponseModel> {
        return addressListResponse
    }

    fun apiCallAddressList(userId:String){
        val retroInstance = RetroInstance.getRetroInstance().create(ApiService::class.java)
        val call = retroInstance.addressList(userId)
        call.enqueue(object : Callback<ManageAddressDetailsResponseModel> {
            override fun onResponse(call: Call<ManageAddressDetailsResponseModel>, response: Response<ManageAddressDetailsResponseModel>) {
                if (response.isSuccessful){
                    addressListResponse.postValue(response.body())
                }
            }
            override fun onFailure(call: Call<ManageAddressDetailsResponseModel>, t: Throwable) {
                // mainCategoryResponse.postValue(null)
            }
        })
    }/*get List of Address*/


    fun deleteAddressObservable(): MutableLiveData<CommonResponse>{
        return deleteResponse
    }

    fun apiCallDeleteAddress(userDetailsId: String){
        val retroInstance = RetroInstance.getRetroInstance().create(ApiService::class.java)
        val call = retroInstance.deleteAddress(userDetailsId)
            call.enqueue(object :Callback<CommonResponse>{
                override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                    if (response.isSuccessful){
                        deleteResponse.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                   // deleteResponse.postValue()
                }

            })
    }/*Delete a particular Address*/


    /*Change Address*/
    fun changeAddressObservable():MutableLiveData<CommonResponse>{
        return changeAddressResponse
    }
    fun apiCallForChangeAddress(userId: String,objectId:String){
        val retroInstance = RetroInstance.getRetroInstance().create(ApiService::class.java)
        val call = retroInstance.changeAddress(userId,objectId)
        call.enqueue(object : Callback<CommonResponse>{
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if (response.isSuccessful){
                    changeAddressResponse.postValue(response.body())
                }
            }
            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                //orderHistoryResponse.postValue(null)
            }

        })
    }
}