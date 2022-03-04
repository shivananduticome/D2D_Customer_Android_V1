package com.d2d.customer.model

data class DeleteCartItemResponse(
    val message: String,
    val status: Boolean,
    val statusCode: Int
)