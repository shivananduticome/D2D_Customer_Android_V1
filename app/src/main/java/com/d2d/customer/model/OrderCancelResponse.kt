package com.d2d.customer.model

data class OrderCancelResponse(
    val message: String,
    val status: Boolean,
    val statusCode: Int
)