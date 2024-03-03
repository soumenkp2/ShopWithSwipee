package com.example.shopwithswipee.data.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.shopwithswipee.data.ApiService.SwipeProductApiService
import com.example.shopwithswipee.datamodels.ProductList
import com.example.shopwithswipee.datamodels.ProductListItem
import javax.inject.Inject

class ProductPagingDataSource @Inject constructor(private val apiService: SwipeProductApiService, private val dataCallback: DataCallback) : PagingSource<Int, ProductListItem>() {

    private lateinit var data: ProductList

    /**
     * Fetches paginated messages from the remote API based on the provided parameters.
     *
     * @param params The parameters for loading pages.
     * @return A LoadResult containing paginated data or an error.
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductListItem> {
        val page = params.key ?: 1

        return try {
            val response = apiService.getSwipeProductList()
            data = response
            dataCallback.onDataReceived(response)

            LoadResult.Page(
                data = data,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (data.isEmpty() || (page*params.loadSize)>=data.size) null else page + 1
            )

        } catch (e: Exception) {
            Log.d("api response error",e.toString())
            LoadResult.Error(e)
        }
    }

    /**
     * Retrieves the refresh key for the current paging state.
     *
     * @param state The current PagingState.
     * @return The refresh key for the state.
     */
    override fun getRefreshKey(state: PagingState<Int,ProductListItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}

interface DataCallback {
    fun onDataReceived(dataLoaded: ProductList)
}