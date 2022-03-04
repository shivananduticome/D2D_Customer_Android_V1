package com.d2d.customer.`interface`

import com.d2d.customer.model.AddOnItem


interface CallbackSubCategoryDescription {
    fun addToCartData(itemName:String, itemDescription:String, itemPrice:String, itemMainCategoryName:String,
                      itemSubCategoryName:String, itemFoodType:Boolean, itemBaseQuantity:Int, itemId:String, itemImageUrl:String)
    fun getSelectedAddOnsValue(amount:String)
    fun userUserRegistration()
    fun addOnsList(addOnSelectList: MutableList<AddOnItem>)
    fun unCheckRemoveAddons(addOnId: String)

}
