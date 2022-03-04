package com.d2d.customer.`interface`

interface CallbackAddress {
  fun deleteAddress(userDetailsId:String)
  fun changesAddress(userId:String,objectId:String)
}