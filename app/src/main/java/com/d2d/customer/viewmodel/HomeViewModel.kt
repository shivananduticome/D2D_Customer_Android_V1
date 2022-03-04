package com.d2d.customer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.d2d.customer.model.MainCategoryResponseModel
import com.d2d.customer.model.SubCategoryResponseModel
import com.d2d.customer.model.SubscriptionTitleDataResponseModel
import com.d2d.customer.retrofit.ApiService
import com.d2d.customer.retrofit.RetroInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    lateinit var mainCategoryResponse:MutableLiveData<MainCategoryResponseModel>
    lateinit var recyclerSubCategoryData: MutableLiveData<SubCategoryResponseModel>
    lateinit var subscriptionTypeResponse: MutableLiveData<SubscriptionTitleDataResponseModel>
    lateinit var errResponse:MutableLiveData<String>


    init {
        mainCategoryResponse = MutableLiveData()
        recyclerSubCategoryData = MutableLiveData()
        subscriptionTypeResponse = MutableLiveData()
    }


    /*get the Main Category list of data*/
    fun mainCategoryObservable():MutableLiveData<MainCategoryResponseModel>{
        return mainCategoryResponse
    }

    fun apiCallForMainCategoryListing(){
        val retroInstance = RetroInstance.getRetroInstance().create(ApiService::class.java)
        val call = retroInstance.fetchMainCategory()
        call.enqueue(object : Callback<MainCategoryResponseModel> {
            override fun onResponse(call: Call<MainCategoryResponseModel>, response: Response<MainCategoryResponseModel>) {
                if (response.isSuccessful){
                    mainCategoryResponse.postValue(response.body())
                }
            }
            override fun onFailure(call: Call<MainCategoryResponseModel>, t: Throwable) {
               // mainCategoryResponse.postValue(null)
            }
        })
    }


    /*Get the food sub Category list function*/
    fun getSubCategoryObservable(): MutableLiveData<SubCategoryResponseModel> {
        return recyclerSubCategoryData
    }

    /* Make API Call for getting the food subcategory List of data*/
    fun apiCallSubCategory(mainCategoryId: String) {
        val retroInstance = RetroInstance.getRetroInstance().create(ApiService::class.java)
        val call = retroInstance.getSubCategoryList(mainCategoryId)
        call.enqueue(object : Callback<SubCategoryResponseModel> {
            override fun onResponse(
                call: Call<SubCategoryResponseModel>,
                response: Response<SubCategoryResponseModel>
            ) {
                if (response.isSuccessful) {
                    recyclerSubCategoryData.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<SubCategoryResponseModel>, t: Throwable) {
                errResponse.postValue(t.message)
            }

        })
    }




    /*Get subscription Type*/
    fun subscriptionTypeObservable(): MutableLiveData<SubscriptionTitleDataResponseModel> {
        return subscriptionTypeResponse
    }

    fun apiCallSubscriptionTypes() {
        val retroInstance = RetroInstance.getRetroInstance().create(ApiService::class.java)
        val call = retroInstance.getSubscriptionTypes()
        call.enqueue(object : Callback<SubscriptionTitleDataResponseModel> {
            override fun onResponse(
                call: Call<SubscriptionTitleDataResponseModel>,
                response: Response<SubscriptionTitleDataResponseModel>
            ) {
                if (response.isSuccessful) {
                    subscriptionTypeResponse.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<SubscriptionTitleDataResponseModel>, t: Throwable) {
               // subscriptionTypeResponse.postValue(null)
                errResponse.postValue(t.message)

            }

        })
    }
}