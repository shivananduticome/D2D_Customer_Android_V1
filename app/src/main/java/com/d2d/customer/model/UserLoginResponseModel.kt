package com.d2d.customer.model

data class UserLoginResponseModel(
    val LoginDetails: LoginDetails,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)
data class LoginDetails(
    val _id: String,
    val countryCode: String,
    val email: String,
    val fullName: String,
    val mobileNo: String,
    val mobileOtp: String,
    val registerDate: String,
    val userId: String,
    val userType: String
)