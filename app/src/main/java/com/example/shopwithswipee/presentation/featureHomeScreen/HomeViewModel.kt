package com.example.shopwithswipee.presentation.featureHomeScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.example.shopwithswipee.datamodels.ProductListItem
import com.example.shopwithswipee.domain.usecase.ProductUsecase
import com.example.shopwithswipee.utils.AppResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel class responsible for managing data related to the home screen.
 * @param productUsecase: The use case responsible for product-related operations.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(private val productUsecase: ProductUsecase) : ViewModel() {

    // LiveData for holding the list of products
    private val _getProductList: MutableLiveData<PagingData<ProductListItem>> = MutableLiveData()
    val getProductList: LiveData<PagingData<ProductListItem>>
        get() = _getProductList

    // LiveData for indicating progress
    private val _showProgress: MutableLiveData<Boolean> = MutableLiveData()
    val showProgress : LiveData<Boolean>
        get() = _showProgress

    /**
     * Fetches the list of products.
     */
    fun getProductListData() {
        viewModelScope.launch(Dispatchers.Main) {
            var dataSource : PagingSource<Int,ProductListItem>? = null
            productUsecase.fetchPaginatedProductList().collectLatest {
                when(it) {
                    is AppResource.Loading -> {
                        println("loading")
                        _showProgress.postValue(true)
                    }
                    is AppResource.Error -> {
                        dataSource = null
                        _showProgress.postValue(false)
                    }
                    is AppResource.Success -> {
                        println("success")
                        dataSource = it.data
                        _showProgress.postValue(false)
                    }
                    else -> {}
                }
            }

            if(dataSource!=null) {
                val pager = Pager(
                    config = PagingConfig(pageSize = 20, enablePlaceholders = false),
                    pagingSourceFactory = { dataSource!! }
                )
                val pagingData = pager.flow.cachedIn(viewModelScope)
                pagingData.asLiveData().observeForever {
                    _getProductList.postValue(it)
                }
            }
        }
    }
}
