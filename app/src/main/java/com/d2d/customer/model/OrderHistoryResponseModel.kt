package com.d2d.customer.model

data class OrderHistoryResponseModel(
    val PlacedOrdersList: List<PlacedOrders>,
    val message: String,
    val status: Boolean,
    val statusCode: Int,
    val totalOrders: Int
)
data class PlacedOrders(
    val address: String,
    val categoryType: String,
    val deliveryStatus: String,
    val endDate: String,
    val houseNoFloor: String,
    val landMark: String,
    val orderDate: String,
    val orderId: String,
    val orderStatus: String,
    val paymentMethod: String,
    val paymentPaid: Int,
    val paymentStatus: String,
    val plan: String,
    val reasonForcancellation: String,
    val saveAdressAs: String,
    val sectorId: String,
    val startdate: String,
    val title: String,
    val userId: String,
    val subscriptionId:String
)