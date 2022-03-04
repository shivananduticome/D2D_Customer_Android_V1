package com.d2d.customer.model

data class SubCategoryResponseModel(
    val SubCategoryData: List<SubCategoryData>,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)

data class SubCategoryData(
    val mainCategoryId: String,
    val subCategoryId: String,
    val subCategoryName: String,
    val subCategoryImage:String
)