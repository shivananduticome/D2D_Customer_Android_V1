package com.d2d.customer.model
data class SubscriptionPlansResponseModel(
    val message: String,
    val status: Boolean,
    val statusCode: Int,
    val subscriptionPlans: List<SubscriptionPlan>
)