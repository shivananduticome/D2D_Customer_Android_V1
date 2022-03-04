package com.d2d.customer.model

data class MenuDetailsResponseDataModel(
    val message: String,
    val status: Boolean,
    val statusCode: Int,
    val subCategoryMenuData: List<SubCategoryMenuData>
)

data class SubCategoryMenuData(
    val addOnIds: List<Any>,
    val itemDescription: String,
    val itemFoodType: Boolean,
    val itemId: String,
    val itemImageUrl: String,
    val itemMainCategoryName: String,
    val itemName: String,
    val itemPrice: String,
    val itemQuantity: String,
    val itemBaseQuantity:Int,
    val itemSubCategoryName: String,
    val mainCategoryId: String,
    val subCategoryId: String
)