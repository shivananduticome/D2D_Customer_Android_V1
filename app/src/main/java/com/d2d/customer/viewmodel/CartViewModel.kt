package com.d2d.customer.viewmodel

import android.content.ClipData
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.d2d.customer.model.*
import com.d2d.customer.retrofit.ApiService
import com.d2d.customer.retrofit.RetroInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartViewModel: ViewModel() {
    var cartCount:String?=null
    private var cartResponseData:MutableLiveData<ViewCartResponseModel> = MutableLiveData()
    private var cartPlaceResponse:MutableLiveData<OrderPlacedResponse> = MutableLiveData()
    private var errorResponse:MutableLiveData<String> = MutableLiveData()
    private var deleteCartItemResponse:MutableLiveData<DeleteCartItemResponse> = MutableLiveData()
    private var incrementOrDecrementCartItemResponse:MutableLiveData<CartIncrementOrDecrementResponse> = MutableLiveData()


    /*This is used to fetch the data from addToCart*/
    fun getCartDetailsObservable():MutableLiveData<ViewCartResponseModel>{
        return cartResponseData
    }

    fun apiCall(userId:String){
        val retroInstance = RetroInstance.getRetroInstance().create(ApiService::class.java)
        val call = retroInstance.getAddToCartDetails(userId)
        call.enqueue(object : Callback<ViewCartResponseModel> {
            override fun onResponse(call: Call<ViewCartResponseModel>,
                                    response: Response<ViewCartResponseModel>
            ) {
                if (response.isSuccessful){
                    cartResponseData.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<ViewCartResponseModel>, t: Throwable) {
               // cartResponseData.postValue(null)
            }

        })
    }

    fun placeObservable():MutableLiveData<OrderPlacedResponse>{
        return cartPlaceResponse
    }

    fun apiCallPlaceOrder(placeOrderSendDataModel: PlaceOrderSendDataModel){
       try {
           val retroInstance = RetroInstance.getRetroInstance().create(ApiService::class.java)
           val call = retroInstance.placeOrder(placeOrderSendDataModel)
           call.enqueue(object : Callback<OrderPlacedResponse> {
               override fun onResponse(call: Call<OrderPlacedResponse>, response: Response<OrderPlacedResponse>) {
                   if (response.isSuccessful){
                       cartPlaceResponse.postValue(response.body())
                   }else{
                       errorResponse.postValue(response.errorBody().toString())

                   }
               }

               override fun onFailure(call: Call<OrderPlacedResponse>, t: Throwable) {
                   // cartResponseData.postValue(null)
                   errorResponse.postValue(t.toString())

               }

           })
       }catch (e:Exception){
       }
    }

    /*Delete Particular Cart Item*/
    fun deleteCardItemObservable():MutableLiveData<DeleteCartItemResponse>{
        return deleteCartItemResponse
    }

    fun apiCallCartItemDelete(userId: String,cardId:String){
        val retroInstance = RetroInstance.getRetroInstance().create(ApiService::class.java)
        val call = retroInstance.deleteCartItem(userId,cardId)
        call.enqueue(object : Callback<DeleteCartItemResponse> {
            override fun onResponse(call: Call<DeleteCartItemResponse>, response: Response<DeleteCartItemResponse>) {
                if (response.isSuccessful){
                    deleteCartItemResponse.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<DeleteCartItemResponse>, t: Throwable) {
               // deleteCartItemResponse.postValue(null)
            }

        })
    }


    /*Increment Or Decrement Particular Cart Item*/

    fun incrementOrDecrementCartObservable():MutableLiveData<CartIncrementOrDecrementResponse>{
        return incrementOrDecrementCartItemResponse
    }

    fun callApiIcrementDecrementCartItem(userId:String,cartId:String,itemBaseQuantity:Int){
        val retroInstance = RetroInstance.getRetroInstance().create(ApiService::class.java)
        val call = retroInstance.editCart(userId,cartId,itemBaseQuantity)
        call.enqueue(object : Callback<CartIncrementOrDecrementResponse> {
            override fun onResponse(call: Call<CartIncrementOrDecrementResponse>, response: Response<CartIncrementOrDecrementResponse>) {
                if (response.isSuccessful){
                    incrementOrDecrementCartItemResponse.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<CartIncrementOrDecrementResponse>, t: Throwable) {
               // incrementOrDecrementCartItemResponse.postValue(null)

            }

        })
    }

    fun fetchCartCount(count:String){
        cartCount = count
    }/*get the cart count*/

}