package com.d2d.customer.viewmodel

import android.widget.MultiAutoCompleteTextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.d2d.customer.model.OrderCancelReasonResponseModel
import com.d2d.customer.model.OrderCancelResponse
import com.d2d.customer.model.OrderHistoryResponseModel
import com.d2d.customer.model.ViewOrderHistoryResponseModel
import com.d2d.customer.retrofit.ApiService
import com.d2d.customer.retrofit.RetroInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderHistoryViewModel : ViewModel() {
    private lateinit var orderHistoryResponse:MutableLiveData<OrderHistoryResponseModel>
    lateinit var orderCancelReasonResponse:MutableLiveData<OrderCancelReasonResponseModel>
    lateinit var orderCancelResponse:MutableLiveData<OrderCancelResponse>
    lateinit var viewOrderHistoryResponse:MutableLiveData<ViewOrderHistoryResponseModel>


    init {
        orderHistoryResponse = MutableLiveData()
        orderCancelReasonResponse = MutableLiveData()
        orderCancelResponse = MutableLiveData()
        viewOrderHistoryResponse = MutableLiveData()
    }
   fun orderHistoryObservable(): MutableLiveData<OrderHistoryResponseModel>{
       return orderHistoryResponse
   }
    fun apiCallOrderHistory(userId:String){
      val retroInstance = RetroInstance.getRetroInstance().create(ApiService::class.java)
      val call = retroInstance.orderHistory(userId)
          call.enqueue(object : Callback<OrderHistoryResponseModel>{
              override fun onResponse(call: Call<OrderHistoryResponseModel>, response: Response<OrderHistoryResponseModel>) {
                  if (response.isSuccessful){
                      orderHistoryResponse.postValue(response.body())
                  }
              }
              override fun onFailure(call: Call<OrderHistoryResponseModel>, t: Throwable) {
                //  orderHistoryResponse.postValue(null)
              }
          })
    }


    /*View Order History*/
    fun viewOrderHistoryObservable():MutableLiveData<ViewOrderHistoryResponseModel>{
        return viewOrderHistoryResponse
    }

    fun apiCallViewOrderHistory(userId: String,orderId:String){
        val retroInstance = RetroInstance.getRetroInstance().create(ApiService::class.java)
        val call = retroInstance.viewOrderHistory(userId,orderId)
        call.enqueue(object : Callback<ViewOrderHistoryResponseModel>{
            override fun onResponse(call: Call<ViewOrderHistoryResponseModel>, response: Response<ViewOrderHistoryResponseModel>) {
                if (response.isSuccessful){
                    viewOrderHistoryResponse.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<ViewOrderHistoryResponseModel>, t: Throwable) {
                // orderCancelResponse.postValue(null)
            }

        })
    }/*View Order History*/


    /*Order Cancel Reason*/
    fun cancelReasonObservable():MutableLiveData<OrderCancelReasonResponseModel>{
        return orderCancelReasonResponse
    }
    fun apiCallForOrderCancelReason(){
        val retroInstance = RetroInstance.getRetroInstance().create(ApiService::class.java)
        val call = retroInstance.orderCancelReason()
            call.enqueue(object : Callback<OrderCancelReasonResponseModel>{
                override fun onResponse(call: Call<OrderCancelReasonResponseModel>, response: Response<OrderCancelReasonResponseModel>) {
                    if (response.isSuccessful){
                        orderCancelReasonResponse.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<OrderCancelReasonResponseModel>, t: Throwable) {
                    //orderHistoryResponse.postValue(null)
                }

            })
    }

    /*Placed Cancel*/
    fun placedOrderCancelObservable():MutableLiveData<OrderCancelResponse>{
        return orderCancelResponse
    }

    fun apiCallForOrderCancel(userId: String,orderId:String,reason:String){
        val retroInstance = RetroInstance.getRetroInstance().create(ApiService::class.java)
        val call = retroInstance.placedOderCancel(userId,orderId,reason)
        call.enqueue(object : Callback<OrderCancelResponse>{
            override fun onResponse(call: Call<OrderCancelResponse>, response: Response<OrderCancelResponse>) {
                if (response.isSuccessful){
                    orderCancelResponse.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<OrderCancelResponse>, t: Throwable) {
               // orderCancelResponse.postValue(null)
            }

        })
    }
}