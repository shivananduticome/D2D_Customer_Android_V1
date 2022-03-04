package com.d2d.customer.model

import com.google.gson.annotations.SerializedName

data class PlaceOrderSendDataModel(
    @SerializedName("address") var address: String,
    @SerializedName("deliveryCharge") var deliveryCharge :String,
    @SerializedName("houseNoFloor") var houseNoFloor: String?,
    @SerializedName("saveAddressAs") var saveAddressAs: String?,
    @SerializedName("landMark") var landMark: String?,
    @SerializedName("sectorId") var sector: String?,
    @SerializedName("totalAmount") var totalAmount: String?,
    @SerializedName("userId") var userId: String?,
    @SerializedName("categoryType") var categoryType:String,
   // var itemArray: List<itemArray>,
)

/*ata class itemArray(
    @SerializedName("cartId") var cartId: String,
    @SerializedName("itemFoodType") var itemFoodType: Boolean,
    @SerializedName("itemId") var itemId: String,
    @SerializedName("itemMainCategoryName") var itemMainCategoryName: String,
    @SerializedName("itemName") var itemName: String,
    @SerializedName("itemPrice") var itemPrice: String,
    @SerializedName("itemBaseQuantity") var itemBaseQuantity: Int,
    @SerializedName("itemSubCategoryName") var itemSubCategoryName: String,
    @SerializedName("itemImageUrl") var itemImageUrl:String,
    var addOnsList: List<AddOnsList>
    )
data class AddOnsList(
    @SerializedName("addOnId") var addOnId:String,
    @SerializedName("addOnName") var addOnName:String,
    @SerializedName("addOnTitleId") var addOnTitleId:String,
    @SerializedName("amount") var amount:Int,
    @SerializedName("itemId") var itemId:String,
    @SerializedName("status") var status:Boolean,
    @SerializedName("type") var type:String,
    @SerializedName("addOnBaseQuantity") var addOnBaseQuantity:Int,
    )*/

