package com.example.shopwithswipee.presentation.featureAddProductScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopwithswipee.datamodels.AddProduct
import com.example.shopwithswipee.datamodels.AddProductResponse
import com.example.shopwithswipee.datamodels.ProductListItem
import com.example.shopwithswipee.domain.usecase.ProductUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel class responsible for handling the addition of a product.
 * @param productUsecase: The use case responsible for product-related operations.
 */
@HiltViewModel
class AddProductViewModel @Inject constructor(private val productUsecase: ProductUsecase) : ViewModel() {

    // LiveData for holding the response of adding a product
    private var _addProductResponse : MutableLiveData<AddProductResponse> = MutableLiveData()
    val addProductResponse : LiveData<AddProductResponse>
        get() = _addProductResponse

    /**
     * Submits a product for addition.
     * @param productListItem: The product to be added.
     */
    fun submitProduct(productListItem: AddProduct) = viewModelScope.launch{
        _addProductResponse.postValue(productUsecase.addProductData(productListItem))
    }
}
