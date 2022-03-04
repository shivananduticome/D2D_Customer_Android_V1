package com.d2d.customer.model

data class ViewCartResponseModel(
    val ViewCartData: List<ViewCartData>,
    val deliveryCharge: String,
    val itemCount: String,
    val itemVat: String,
    val message: String,
    val priceAfterVat: String,
    val priceBeforeVat: String,
    val status: Boolean,
    val statusCode: Int,
    val address:String,
    val houseOrFloorNo:String,
    val saveAddressAs:String,
    val landMark:String,
    val sectorId:String
)

data class ViewCartData(
    val cartDate: String,
    val cartId: String,
    val itemBasePrice: String,
    var itemBaseQuantity:Int,
    val itemDescription: String,
    val itemFoodType: Boolean,
    val itemId: String,
    val itemImageUrl: String,
    val itemMainCategoryName: String,
    val itemName: String,
    val itemPrice: String,
    var itemQuantity: String,
    val itemSubCategoryName: String,
    val userId: String,
    val addOnSelection: List<addOnSelection>,
    )

data class addOnSelection(
    val addOnId: String,
    val addOnName: String,
    val addOnTitleId: String,
    var amount: Int,
    val itemId: String,
    val status: Boolean,
    val type:String,
    val addOnBaseQuantity:Int
   )