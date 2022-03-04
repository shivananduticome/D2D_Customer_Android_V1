package com.d2d.customer.model

import com.google.gson.annotations.SerializedName
import retrofit2.http.Field

data class AddToCartPostData(
    @SerializedName("itemMainCategoryName") var itemMainCategoryName:String,
    @SerializedName("itemSubCategoryName") var itemSubCategoryName:String,
    @SerializedName("itemFoodType") var itemFoodType:Boolean,
    @SerializedName("itemName") var itemName:String,
    @SerializedName("itemId") var itemId:String,
    @SerializedName("itemBaseQuantity") var itemBaseQuantity:Int,
    @SerializedName("itemPrice") var itemPrice:String,
    @SerializedName("itemImageUrl") var itemImageUrl:String,
    @SerializedName("itemDescription") var itemDescription:String,
    @SerializedName("userId") var userId:String,
    @SerializedName("itemBasePrice") var itemBasePrice:String,
    var AddOnSelections:List<AddOnSelections>,
)
data class AddOnSelections(
   @SerializedName("addOnTitleId") val addOnTitleId: String,
   @SerializedName("addOnName") val addOnName: String,
   @SerializedName("amount") val amount: Int,
   @SerializedName("itemId") val itemId:String,
   @SerializedName("addOnId") val addOnId:String,
   @SerializedName("status") val status:Boolean,
   @SerializedName("type") val type:String,
   @SerializedName("addOnBaseQuantity") val addOnBaseQuantity:Int
)

