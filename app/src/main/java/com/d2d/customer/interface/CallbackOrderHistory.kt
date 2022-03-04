package com.d2d.customer.`interface`
interface CallbackOrderHistory {
    fun orderCancelDialog()
    fun placedOrderCancel(userId:String,orderId:String)
    fun orderCancelReason(reason:String)
    fun viewOrderHistory(userId:String?, orderId:String?, categoryType:String?, paymentPaid:Int?, paymentMethod:String?,
                         address:String?,subscriptionId:String?,orderStatus:String,startDate:String,endDate:String)
}