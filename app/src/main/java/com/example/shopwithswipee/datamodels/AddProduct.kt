package com.example.shopwithswipee.datamodels

import java.io.File

data class AddProduct(
    val productName: String,
    val productType: String,
    val price: Double,
    val tax: Double,
    val files: File?
)