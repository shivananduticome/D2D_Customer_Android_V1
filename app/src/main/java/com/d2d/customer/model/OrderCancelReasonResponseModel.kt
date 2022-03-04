package com.d2d.customer.model

data class OrderCancelReasonResponseModel(
    val CancelReasons: List<CancelReasons>,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)

data class CancelReasons(
    val reason: String,
    var isSelected:Boolean = false

)