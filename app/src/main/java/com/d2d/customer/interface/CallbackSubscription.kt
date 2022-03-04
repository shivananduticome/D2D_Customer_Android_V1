package com.d2d.customer.`interface`
interface CallbackSubscription {
    fun subscriptionType(subscriptionDescription:String,subscriptionId: String,subscriptionImage: String,subscriptionLeastAmount: String,subscriptionType: String)
    fun subscriptionPlan(numberOfPlanDays:String,price:String,subscriptionType:String,subscriptionId:String)
}