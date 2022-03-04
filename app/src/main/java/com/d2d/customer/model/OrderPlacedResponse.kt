package com.d2d.customer.model

data class OrderPlacedResponse(
    val message: String,
    val status: Boolean,
    val statusCode: Int
)