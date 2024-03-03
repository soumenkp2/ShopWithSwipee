package com.example.shopwithswipee.datamodels

data class AddProductResponse(
    val message: String,
    val product_details: ProductListItem,
    val product_id: Int,
    val success: Boolean
)