package com.d2d.customer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.d2d.customer.model.UserLoginResponseModel
import com.d2d.customer.model.UserRegistrationResponseModel
import com.d2d.customer.model.VerifyOtpResponseModel
import com.d2d.customer.retrofit.ApiService
import com.d2d.customer.retrofit.RetroInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationLoginViewModel :ViewModel() {
    lateinit var registrationResponse:MutableLiveData<UserRegistrationResponseModel>
    lateinit var otpVerificationResponse:MutableLiveData<VerifyOtpResponseModel>
    lateinit var userLoginResponse:MutableLiveData<UserLoginResponseModel>

    init {
        registrationResponse= MutableLiveData()
        otpVerificationResponse = MutableLiveData()
        userLoginResponse = MutableLiveData()
    }

    fun registrationObservable():MutableLiveData<UserRegistrationResponseModel>{
        return registrationResponse
    }/*user Registration Observable*/

    fun apiCallRegistration(fullName:String,mobileNo:String,email:String,countryCode:String){
        val retroInstance = RetroInstance.getRetroInstance().create(ApiService::class.java)
        val call =retroInstance.userRegistration(fullName,mobileNo,email,countryCode)
            call.enqueue(object :Callback<UserRegistrationResponseModel>{
                override fun onResponse(call: Call<UserRegistrationResponseModel>, response: Response<UserRegistrationResponseModel>) {
                    registrationResponse.postValue(response.body())
                }

                override fun onFailure(call: Call<UserRegistrationResponseModel>, t: Throwable) {
                   // registrationResponse.postValue(null)
                }

            })
    }/*api call user Registration*/


    fun optVerifyObservable():MutableLiveData<VerifyOtpResponseModel>{
        return otpVerificationResponse
    }/*verify otp Observable*/

    fun apiCallOtpVerify(countryCode:String,mobileNo:String,mobileOtp:String){
        val retroInstance = RetroInstance.getRetroInstance().create(ApiService::class.java)
        val call = retroInstance.otpVerification(countryCode,mobileNo,mobileOtp)
            call.enqueue(object :Callback<VerifyOtpResponseModel>{ override fun onResponse(call: Call<VerifyOtpResponseModel>, response: Response<VerifyOtpResponseModel>) {
                  if (response.isSuccessful){
                      otpVerificationResponse.postValue(response.body())
                  }
                }
                override fun onFailure(call: Call<VerifyOtpResponseModel>, t: Throwable) {
                   // otpVerificationResponse.postValue()

                }

            })
    }/*api call for user verify otp*/

    fun userLoginObservable():MutableLiveData<UserLoginResponseModel>{
      return userLoginResponse
    }

    fun  apiCallUserLogin(countryCode:String,mobileNo:String){
        val retroInstance =RetroInstance.getRetroInstance().create(ApiService::class.java)
        val call = retroInstance.userLogin(countryCode,mobileNo)
            call.enqueue(object :Callback<UserLoginResponseModel>{
                override fun onResponse(call: Call<UserLoginResponseModel>, response: Response<UserLoginResponseModel>) {
                    if (response.isSuccessful){
                        userLoginResponse.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<UserLoginResponseModel>, t: Throwable) {
                   // userLoginResponse.postValue(null)

                }

            })
    }
}