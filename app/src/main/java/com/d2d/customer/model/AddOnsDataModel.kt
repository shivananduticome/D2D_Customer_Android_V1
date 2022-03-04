package com.d2d.customer.model

data class AddOnsDataModel(
    val AddOnDetails: List<AddOnDetail>,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)
data class AddOnDetail(
    val addOnItem: List<AddOnItem>,
    val addOnTitle: String,
    val addOnTitleId: String,
    val type: String
)

data class AddOnItem(
    val addOnId: String,
    val addOnName: String,
    val addOnQuantity: Int,
    val amount: Int,
    val itemId: String,
    val status: Boolean,
    val addOnTitle: String,
    val type: String,
    val addOnTitleId:String,
    val addOnBaseQuantity:Int,
    val addOnFoodType :Boolean
)
