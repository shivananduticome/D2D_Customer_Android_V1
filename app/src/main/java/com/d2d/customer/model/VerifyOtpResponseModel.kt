package com.d2d.customer.model

data class VerifyOtpResponseModel(
    val UserOtpDetails: UserOtpDetails,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)
data class UserOtpDetails(
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