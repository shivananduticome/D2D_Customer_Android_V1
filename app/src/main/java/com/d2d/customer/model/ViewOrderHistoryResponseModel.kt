package com.d2d.customer.model

data class ViewOrderHistoryResponseModel(
    val ViewOrderHistory: ViewOrderHistory,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)
data class ViewOrderHistory(
    val _id: String,
    val address: String,
    val deliveryCharge:String,
    val categoryType: String,
    val deliveryStatus: String,
    val houseNoFloor: String,
    val landMark: String,
    val orderDate: String,
    val orderDetails: List<OrderDetail>,
    val orderId: String,
    val orderStatus: String,
    val paymentMethod: String,
    val paymentPaid: Int,
    val paymentStatus: String,
    val reasonForcancellation: String,
    val saveAddressAs: String,
    val sectorId: String,
    val status: String,
    val userId: String
)

data class OrderDetail(
    val cartId: String,
    val itemBaseQuantity: Int,
    val itemFoodType: Boolean,
    val itemId: String,
    val itemMainCategoryName: String,
    val itemName: String,
    val itemPrice: String,
    val itemStatus: String,
    val itemSubCategoryName: String,
    val itemImageUrl:String,
    var addOnsList:List<orderDetails>
)

data class orderDetails(
     val addOnId: String,
     val addOnName: String,
     val addOnTitle: String,
     val addOnTitleId:String,
     val amount: Int,
     val itemId:String,
     val status:Boolean,
     val type: String
)