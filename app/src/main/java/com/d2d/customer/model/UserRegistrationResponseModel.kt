package com.d2d.customer.model
data class UserRegistrationResponseModel(
    val RegisterData: RegisterData,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)
data class RegisterData(
    val countryCode: String,
    val email: String,
    val fullName: String,
    val mobileNo: String,
    val mobileOtp: String,
    val registerDate: String,
    val token: String,
    val userId: String
)