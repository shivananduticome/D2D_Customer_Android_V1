package com.d2d.customer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.d2d.customer.model.AddOnsDataModel
import com.d2d.customer.model.AddToCartPostData
import com.d2d.customer.model.CommonResponse
import com.d2d.customer.model.MenuDetailsResponseDataModel
import com.d2d.customer.retrofit.ApiService
import com.d2d.customer.retrofit.RetroInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SubCategoryDescriptionViewModel : ViewModel() {
    private var subCategoryRelatedResponse:MutableLiveData<MenuDetailsResponseDataModel> = MutableLiveData()
    private var vegMenuResponse:MutableLiveData<MenuDetailsResponseDataModel> = MutableLiveData()
    private var addToCartResponse:MutableLiveData<CommonResponse> = MutableLiveData()
    private var addOnsResponse:MutableLiveData<AddOnsDataModel> = MutableLiveData()
    private var errorResponse:MutableLiveData<String> = MutableLiveData()


    fun subCategoryRelatedMenuDetailsObservable():MutableLiveData<MenuDetailsResponseDataModel>{
        return subCategoryRelatedResponse
    }

    fun apiCallToGetSubCategoryRelatedMenuDetails(mainCategoryId:String,subCategoryId:String){
        val retroInstance = RetroInstance.getRetroInstance().create(ApiService::class.java)
        val call = retroInstance.getSubCategoryRelatedDetails(mainCategoryId,subCategoryId)
            call.enqueue(object : Callback<MenuDetailsResponseDataModel>{
                override fun onResponse(call: Call<MenuDetailsResponseDataModel>, response: Response<MenuDetailsResponseDataModel>) {
                  if (response.isSuccessful){
                      subCategoryRelatedResponse.postValue(response.body())
                  }
                }

                override fun onFailure(call: Call<MenuDetailsResponseDataModel>, t: Throwable) {
                  // subCategoryRelatedResponse.postValue(null)
                    errorResponse.postValue(t.message)
                }

            })
    }/*Fetch subCategory Menu Details*/


    fun getVegMenuObservable():MutableLiveData<MenuDetailsResponseDataModel>{
        return  vegMenuResponse
    }


    fun apiCallVegMenu(mainCategoryId:String,subCategoryId:String){
        val retroInstance = RetroInstance.getRetroInstance().create(ApiService::class.java)
        val call = retroInstance.getVegMenuDetails(mainCategoryId,subCategoryId)
            call.enqueue(object : Callback<MenuDetailsResponseDataModel>{
                override fun onResponse(call: Call<MenuDetailsResponseDataModel>, response: Response<MenuDetailsResponseDataModel>) {
                    if (response.isSuccessful){
                        vegMenuResponse.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<MenuDetailsResponseDataModel>, t: Throwable) {
                    //vegMenuResponse.postValue(null)
                }

            })
    }




    fun addToCartObservable() : MutableLiveData<CommonResponse>{
        return addToCartResponse
    }


    fun apiCallAddToCart(addToCartPostData: AddToCartPostData) {
        val retroInstance = RetroInstance.getRetroInstance().create(ApiService::class.java)
        val call = retroInstance.addToCart(addToCartPostData)
        call.enqueue(object : retrofit2.Callback<CommonResponse>{
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if (response.isSuccessful){
                    addToCartResponse.postValue(response.body())
                }
            }
            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                //addToCartResponse.postValue(null)

            }
        })

    }

    /*get Addons Data*/

    fun addOnsObservable():MutableLiveData<AddOnsDataModel>{
        return addOnsResponse
    }

    fun apiCallAddons(itemId:String){
     val retroInstance = RetroInstance.getRetroInstance().create(ApiService::class.java)
     val call = retroInstance.getAddOns(itemId)
         call.enqueue(object :Callback<AddOnsDataModel>{
             override fun onResponse(call: Call<AddOnsDataModel>, response: Response<AddOnsDataModel>) {
                 if (response.isSuccessful){
                     addOnsResponse.postValue(response.body())
                 }
             }

             override fun onFailure(call: Call<AddOnsDataModel>, t: Throwable) {
                 //addOnsResponse.postValue(null)
             }
         })
    }

}