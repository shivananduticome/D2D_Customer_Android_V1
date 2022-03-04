package com.d2d.customer.model

data class CartIncrementOrDecrementResponse(
    val message: String,
    val status: Boolean,
    val statusCode: Int
)