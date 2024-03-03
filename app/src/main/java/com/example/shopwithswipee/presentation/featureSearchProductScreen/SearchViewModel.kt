package com.example.shopwithswipee.presentation.featureSearchProductScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.shopwithswipee.datamodels.ProductListItem
import com.example.shopwithswipee.domain.usecase.ProductUsecase
import com.example.shopwithswipee.utils.AppResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing search functionality.
 * @param productUsecase: Use case for fetching product data.
 */
@HiltViewModel
class SearchViewModel @Inject constructor(private val productUsecase: ProductUsecase) : ViewModel() {

    /**
     * LiveData for observing the list of products.
     */
    private val _getProductList: MutableLiveData<List<ProductListItem>?> = MutableLiveData()
    val getProductList: LiveData<List<ProductListItem>?>
        get() = _getProductList

    /**
     * Initializes the ViewModel and triggers fetching of product list.
     */
    init {
        viewModelScope.launch { getProductList() }
    }

    /**
     * Fetches the product list from the use case.
     */
    suspend fun getProductList() {
        productUsecase.fetchProductList().collectLatest {
            when(it) {
                is AppResource.Loading -> {
                    // Handle loading state
                }
                is AppResource.Error -> {
                    // Handle error state
                }
                is AppResource.Success -> {
                    _getProductList.postValue(it.data)
                }
                else -> {}
            }
        }
    }
}
