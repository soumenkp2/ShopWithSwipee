package com.example.shopwithswipee.data.repository

import androidx.paging.PagingSource
import com.example.shopwithswipee.data.ApiService.SwipeProductApiService
import com.example.shopwithswipee.datamodels.AddProduct
import com.example.shopwithswipee.datamodels.AddProductResponse
import com.example.shopwithswipee.datamodels.ProductList
import com.example.shopwithswipee.datamodels.ProductListItem
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class ProductServiceRepositoryImpl(val apiService: SwipeProductApiService) : ProductServiceRepository {

    override suspend fun getSwipeProductList(): PagingSource<Int, ProductListItem> {
        return ProductPagingDataSource(apiService, object: DataCallback {
            override fun onDataReceived(dataLoaded: ProductList) { }
        })
    }

    override suspend fun getProductList(): ProductList {
        return apiService.getSwipeProductList()
    }

    override suspend fun sendProductDetails(product: AddProduct) : AddProductResponse {
        return apiService.sendProductDetails(
            product.productName.toRequestBody(),
            product.productType.toRequestBody(),
            product.price.toString().toRequestBody(),
            product.tax.toString().toRequestBody(),
            product.files?.let { RequestBody.create("image/*".toMediaTypeOrNull(), it) }?.let {
                MultipartBody.Part.createFormData(
                    "files[]",
                    product.files?.name,
                    it
                )
            }
        )
    }

}