package com.d2d.customer.`interface`

import com.d2d.customer.model.ViewCartData
import com.d2d.customer.model.addOnSelection

interface CallbackViewCart {
   // fun passAddToCartDetails(itemMainCategoryName:String,itemSubCategoryName:String,itemName:String,itemId:String,itemBaseQuantity:String,itemPrice:String)
    fun passPlaceOrderList(cartData: MutableList<ViewCartData>,addOnSelectionList: MutableList<addOnSelection>)
    fun deleteItem(userId:String,cartId:String)
    fun decrementOrIncremenItem(userId:String,cartId:String,itemBaseQuantity:Int,itemPrice:String)
}