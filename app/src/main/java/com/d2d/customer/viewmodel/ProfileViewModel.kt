package com.d2d.customer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.d2d.customer.model.ProfileEditResponseModel
import com.d2d.customer.retrofit.ApiService
import com.d2d.customer.retrofit.RetroInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel : ViewModel() {
    private var profileUpdateResponseData: MutableLiveData<ProfileEditResponseModel>
    var fullname:String? = null
    var email:String? = null
    var userId:String? = null

    init {
        profileUpdateResponseData = MutableLiveData()
    }


    fun editProfileObservable():MutableLiveData<ProfileEditResponseModel>{
        return profileUpdateResponseData
    }

    fun ApiCallEditProfile(){
        val retroInstance = RetroInstance.getRetroInstance().create(ApiService::class.java)
        val call = retroInstance.userprofileUpdate(fullname!!,email!!,userId!!)
        call.enqueue(object : Callback<ProfileEditResponseModel> {
            override fun onResponse(call: Call<ProfileEditResponseModel>, response: Response<ProfileEditResponseModel>) {
                if (response.isSuccessful){
                    profileUpdateResponseData.postValue(response.body())

                }
            }

            override fun onFailure(call: Call<ProfileEditResponseModel>, t: Throwable) {
               // profileUpdateResponseData.postValue(null)
            }

        })
    }
}