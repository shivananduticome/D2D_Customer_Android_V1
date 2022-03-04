package com.d2d.customer.model

data class CommonResponse(
    val message: String,
    val status: Boolean,
    val statusCode: Int
)