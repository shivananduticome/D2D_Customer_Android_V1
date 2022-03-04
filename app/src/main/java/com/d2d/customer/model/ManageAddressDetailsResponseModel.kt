package com.d2d.customer.model

data class ManageAddressDetailsResponseModel(
    val AdressDetails: List<AdressDetail>,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)
data class AdressDetail(
    val address: String,
    val fullName: String,
    val houseOrFloorNo: String,
    val landMark: String,
    val latLong: String,
    val mobileNo: String,
    val saveAddressAs: String,
    val sectorId: String,
    val userDetailOBJId: String,
    val userId: String,
    val status:Boolean
)