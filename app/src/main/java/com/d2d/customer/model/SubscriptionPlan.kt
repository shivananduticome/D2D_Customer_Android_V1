package com.d2d.customer.model

data class SubscriptionPlan(
    val days: String,
    val price: String,
    val subscriptionId: String,
    val subscriptionTitle: String,
    var isSelected:Boolean = false

)