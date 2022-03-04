package com.d2d.customer.retrofit

import com.d2d.customer.model.*
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("user/userRegistration")
    fun userRegistration(
        @Field("fullName") fullName: String,
        @Field("mobileNo") mobileNumber: String,
        @Field("email") email: String,
        @Field("countryCode") countryCode: String,
    ): Call<UserRegistrationResponseModel>

    /*OTPVerify Method*/
    @FormUrlEncoded
    @POST("user/verifyOtp")
    fun otpVerification(
        @Field("countryCode") countryCode: String,
        @Field("mobileNo") mobileNumber: String,
        @Field("mobileOtp") mobileOtp:String
    ): Call<VerifyOtpResponseModel>

    /*Login ApiService Call*/
    @FormUrlEncoded
    @POST("user/userLogin")
    fun userLogin(
        @Field("countryCode") countryCode:String,
        @Field("mobileNo") mobileNumber: String
    ) : Call<UserLoginResponseModel>

    /*get MainCategory list*/
    @GET("manager/fetchMainCategory")
    fun fetchMainCategory():Call<MainCategoryResponseModel>

    /*Getting the Food Sub Category Data Method*/
    @FormUrlEncoded
    @POST("manager/fetchSubCategory")
    fun getSubCategoryList(
        @Field("mainCategoryId") mainCategoryId:String
    ): Call<SubCategoryResponseModel>

    /*Get the subscription Types*/
    @GET("manager/fetchSubscriptionTitle")
    fun getSubscriptionTypes():Call<SubscriptionTitleDataResponseModel>

    /*get The sub category related data*/
    @FormUrlEncoded
    @POST("user/fetchUserMenuOld")
    fun getSubCategoryRelatedDetails(
        @Field("mainCategoryId") mainCategoryId:String,
        @Field("subCategoryId") subCategoryId:String
    ): Call<MenuDetailsResponseDataModel>

    /*Post AddOns Data*/
    @FormUrlEncoded
    @POST("user/fetchAddOnDetailsTest")
    fun getAddOns(
        @Field("itemId") itemId:String
    ): Call<AddOnsDataModel>

    /*get The sub category related Veg menu for Toggle*/
    @FormUrlEncoded
    @POST("user/fetchVegMenu")
    fun getVegMenuDetails(
        @Field("mainCategoryId") mainCategoryId:String,
        @Field("subCategoryId") subCategoryId:String,
    ): Call<MenuDetailsResponseDataModel>

  /*  *//*Post addToCart Items data*//*
    @FormUrlEncoded
    @POST("user/addToCart")
    fun addToCart(
        @Field("itemMainCategoryName") itemMainCategoryName:String,
        @Field("itemSubCategoryName") itemSubCategoryName:String,
        @Field("itemFoodType") itemFoodType:Boolean,
        @Field("itemName") itemName:String,
        @Field("itemId") itemId:String,
        @Field("itemBaseQuantity")itemBaseQuantity:Int,
        @Field("itemPrice") itemPrice:String,
        @Field("itemImageUrl") itemImageUrl:String,
        @Field("itemDescription") itemDescription:String,
        @Field("userId") userId:String,
        @Field("itemBasePrice") itemBasePrice:String
    ): Call<CartIncrementOrDecrementResponse>*/

    /*Post addToCart Items data*/
    @POST("user/addToCart")
    fun addToCart(
        @Body addToCartPostData: AddToCartPostData
    ): Call<CommonResponse>



    /*Get the fetch upcoming Meals*/
    @FormUrlEncoded
    @POST("user/fetchUpComingMeals")
    fun subscriptionUpcomingMeals(
        @Field("subscriptionId") subscriptionId:String
    ):Call<SubscriptionUpcomingMealResponseModel>

    /*Get the fetch upcoming Meals*/
    @FormUrlEncoded
    @POST("user/fetchSubcriptionPlans")
    fun subscriptionPlans(
        @Field("subscriptionId") subscriptionId:String
    ):Call<SubscriptionPlansResponseModel>

    /*Place the Subscription Plans*/
    @FormUrlEncoded
    @POST("user/placeOrder")
    fun placeSubscription(
        @Field("userId") userId:String,
        @Field("sectorId") sectorId:String,
        @Field("address")address:String,
        @Field("houseNoFloor") houseNoFloor:String,
        @Field("saveAddressAs") saveAddressAs:String,
        @Field("landMark") landMark:String,
        @Field("totalAmount") totalAmount:String,
        @Field("categoryType") categoryType:String,
        @Field("subscriptionPlan") plan:String,
        @Field("startDate") startDate:String,
        @Field("subscriptionTitle") subscriptionTitle:String,
        @Field("subscriptionId") subscriptionId:String
    ):Call<OrderPlacedResponse>


    /*Fetch addToCart details*/
    @FormUrlEncoded
    @POST("user/viewCart")
    fun getAddToCartDetails(
        @Field("userId") userId:String
    ): Call<ViewCartResponseModel>

    /*Place Order*/
    @POST("user/placeOrder")
    fun placeOrder(
        @Body placeOrderSendDataModel: PlaceOrderSendDataModel
    ): Call<OrderPlacedResponse>

    /*Delete Cart Item*/
    @FormUrlEncoded
    @POST("/user/deleteCart")
    fun deleteCartItem(
        @Field("userId") userId:String,
        @Field("cartId") cartId: String
    ): Call<DeleteCartItemResponse>

    /*Increment Or Decrement Item*/
    @FormUrlEncoded
    @POST("user/editCart")
    fun editCart(
        @Field("userId") userId:String,
        @Field("cartId") cartId:String,
        @Field("itemBaseQuantity") itemBaseQuantity:Int
    ): Call<CartIncrementOrDecrementResponse>


    /*Edit User Profile*/
    @FormUrlEncoded
    @POST("updateUserDetails")
    fun userprofileUpdate(
        @Field("fullName") fullName:String,
        @Field("email")  email:String,
        @Field("userId") userId:String
    ): Call<ProfileEditResponseModel>


    /*Place Order History*/
    @FormUrlEncoded
    @POST("user/userOrderHistory")
    fun orderHistory(
        @Field("userId") userId:String
    ): Call<OrderHistoryResponseModel>

    /*View Order History*/
    @FormUrlEncoded
    @POST("user/viewAllACartOrderHistory")
    fun viewOrderHistory(
        @Field("userId") userId:String,
        @Field("orderId") orderId:String
    ): Call<ViewOrderHistoryResponseModel>

    /*get the Order Cancel Reason*/
    @GET("user/cancelReason")
    fun orderCancelReason():Call<OrderCancelReasonResponseModel>


    /*Placed Order Cancel*/
    @FormUrlEncoded
    @POST("user/cancelOrder")
    fun placedOderCancel(
        @Field("userId") userId:String,
        @Field("orderId") orderId:String,
        @Field("reason") reason:String,
    ):Call<OrderCancelResponse>

    /*get the Sector Details*/
    @GET("user/fetchSectors")
    fun sectorDetails():Call<SectorDetailsResponse>

    /*Address Model*/
    @FormUrlEncoded
    @POST("user/addAddress")
    fun address(
        @Field("userId") userId:String,
        @Field("latLong") latLong:String,
        @Field("address") address:String,
        @Field("sectorId") sectorId:String,
        @Field("houseOrFloorNo") houseOrFloorNo:String,
        @Field("landMark") landMark:String,
        @Field("saveAddressAs") place:String
    ):Call<AddressResponseModel>

    /*Changes Address*/
    @FormUrlEncoded
    @POST("user/changeUserAddress")
    fun changeAddress(
        @Field("userId") userId:String,
        @Field("objectid") objectid:String,
    ):Call<CommonResponse>

    /*fetch list of store Address*/
    @FormUrlEncoded
    @POST("user/fetchUserAddressDetails")
    fun addressList(
        @Field("userId") userId:String
    ):Call<ManageAddressDetailsResponseModel>

    /*Delete Address*/
    @FormUrlEncoded
    @POST("user/deleteUserAddress")
    fun deleteAddress(
        @Field("id") id:String
    ):Call<CommonResponse>
}

//https://developer.android.com/training/snackbar/showing#kotlin
