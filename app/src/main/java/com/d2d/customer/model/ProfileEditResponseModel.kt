package com.d2d.customer.model

data class ProfileEditResponseModel(
    val message: String,
    val status: Boolean,
    val statusCode: Int,
    val userProfileUpdateData: UserProfileUpdateData
)
data class UserProfileUpdateData(
    val email: String,
    val fullName: String
)