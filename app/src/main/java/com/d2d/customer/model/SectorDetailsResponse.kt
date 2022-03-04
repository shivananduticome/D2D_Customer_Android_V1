package com.d2d.customer.model

data class SectorDetailsResponse(
    val SectorDetail: List<SectorDetail>,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)
data class SectorDetail(
    val sector: String
)