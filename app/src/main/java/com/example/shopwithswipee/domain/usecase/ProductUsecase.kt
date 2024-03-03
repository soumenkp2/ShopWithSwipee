package com.example.shopwithswipee.domain.usecase

import androidx.paging.PagingSource
import com.example.shopwithswipee.data.repository.ProductServiceRepository
import com.example.shopwithswipee.datamodels.AddProduct
import com.example.shopwithswipee.datamodels.AddProductResponse
import com.example.shopwithswipee.datamodels.ProductList
import com.example.shopwithswipee.datamodels.ProductListItem
import com.example.shopwithswipee.utils.AppResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Class representing the use case for product-related operations.
 * @param repository: The repository responsible for handling data operations.
 */
class ProductUsecase @Inject constructor(
    private val repository: ProductServiceRepository
) {

    /**
     * Fetches a paginated list of products.
     * @return Flow<AppResource<PagingSource<Int, ProductListItem>>>: A flow of resource states representing
     *         the paginated list of products.
     */
    suspend fun fetchPaginatedProductList(): Flow<AppResource<PagingSource<Int, ProductListItem>>> = flow {
        try {
            emit(AppResource.Loading())
            val response = repository.getSwipeProductList()
            emit(AppResource.Success(response))
        } catch (e: Exception){
            e.printStackTrace()
            emit(AppResource.Error(e.localizedMessage ?: "An unexpected error occurred in fetching products list"))
        }
    }

    /**
     * Fetches a list of products.
     * @return Flow<AppResource<ProductList>>: A flow of resource states representing the list of products.
     */
    suspend fun fetchProductList(): Flow<AppResource<ProductList>> = flow {
        try {
            emit(AppResource.Loading())
            val response = repository.getProductList()
            emit(AppResource.Success(response))
        } catch (e: Exception){
            e.printStackTrace()
            emit(AppResource.Error(e.localizedMessage ?: "An unexpected error occurred in fetching products list"))
        }
    }

    /**
     * Adds product data to the repository.
     * @param productListItem: The product to be added.
     * @return AddProductResponse?: The response containing the status of the product addition,
     *                              or null if an error occurred.
     */
    suspend fun addProductData(productListItem: AddProduct): AddProductResponse? {
        return try {
            repository.sendProductDetails(productListItem)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
