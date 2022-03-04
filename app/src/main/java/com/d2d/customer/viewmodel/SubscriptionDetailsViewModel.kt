package com.d2d.customer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.d2d.customer.model.OrderPlacedResponse
import com.d2d.customer.model.SubscriptionPlansResponseModel
import com.d2d.customer.model.SubscriptionUpcomingMealResponseModel
import com.d2d.customer.retrofit.ApiService
import com.d2d.customer.retrofit.RetroInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field

class SubscriptionDetailsViewModel : ViewModel() {
    private lateinit var subscriptionUpcomingMealsResponse: MutableLiveData<SubscriptionUpcomingMealResponseModel>
    private lateinit var subscriptionPlansResponse: MutableLiveData<SubscriptionPlansResponseModel>
    private lateinit var placeSubscriptionResponse: MutableLiveData<OrderPlacedResponse>

    init {
        subscriptionUpcomingMealsResponse = MutableLiveData()
        subscriptionPlansResponse = MutableLiveData()
        placeSubscriptionResponse = MutableLiveData()
    }

    fun subscriptionUpcomingMealObservable(): MutableLiveData<SubscriptionUpcomingMealResponseModel> {
        return subscriptionUpcomingMealsResponse
    }

    fun apiCallUpcomingMeals(subscriptionId: String) {
        val retroInstance = RetroInstance.getRetroInstance().create(ApiService::class.java)
        val call = retroInstance.subscriptionUpcomingMeals(subscriptionId)
        call.enqueue(object : Callback<SubscriptionUpcomingMealResponseModel> {
            override fun onResponse(
                call: Call<SubscriptionUpcomingMealResponseModel>,
                response: Response<SubscriptionUpcomingMealResponseModel>
            ) {
                if (response.isSuccessful) {
                    subscriptionUpcomingMealsResponse.postValue(response.body())
                }
            }

            override fun onFailure(
                call: Call<SubscriptionUpcomingMealResponseModel>,
                t: Throwable
            ) {
               // subscriptionUpcomingMealsResponse.postValue(null)

            }

        })
    }


    fun subscriptionPlanObservable(): MutableLiveData<SubscriptionPlansResponseModel> {
        return subscriptionPlansResponse
    }

    fun apiCallPlan(subscriptionId: String) {
        val retroInstance = RetroInstance.getRetroInstance().create(ApiService::class.java)
        val call = retroInstance.subscriptionPlans(subscriptionId)
        call.enqueue(object : Callback<SubscriptionPlansResponseModel> {
            override fun onResponse(
                call: Call<SubscriptionPlansResponseModel>,
                response: Response<SubscriptionPlansResponseModel>
            ) {
                if (response.isSuccessful) {
                    subscriptionPlansResponse.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<SubscriptionPlansResponseModel>, t: Throwable) {
              //  subscriptionPlansResponse.postValue(null)

            }
        })
    }

    /*Place the subscription Plan*/
    fun placeSubscriptionObservable(): MutableLiveData<OrderPlacedResponse> {
        return placeSubscriptionResponse
    }

    fun apiCallPlaceSubscriptionPlan(userId: String, sectorId: String,address:String, houseNoFloor: String,saveAddressAs:String, landMark: String, totalAmount: String, categoryType: String, plan: String, startDate: String, subscriptionTitle: String, subscriptionId:String) {
        val retroInstance = RetroInstance.getRetroInstance().create(ApiService::class.java)
        val call = retroInstance.placeSubscription(userId,sectorId,address, houseNoFloor, saveAddressAs,landMark,
            totalAmount, categoryType, plan, startDate, subscriptionTitle,subscriptionId)
        call.enqueue(object : Callback<OrderPlacedResponse> {
            override fun onResponse(call: Call<OrderPlacedResponse>, response: Response<OrderPlacedResponse>) {
                if (response.isSuccessful) {
                    placeSubscriptionResponse.postValue(response.body())
                }
            }
            override fun onFailure(call: Call<OrderPlacedResponse>, t: Throwable) {
               // placeSubscriptionResponse.postValue(null)
            }

        })
    }


}