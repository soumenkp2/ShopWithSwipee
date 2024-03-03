package com.example.shopwithswipee.presentation.featureSearchProductScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopwithswipee.databinding.ItemProductBinding
import com.example.shopwithswipee.datamodels.ProductListItem
import com.squareup.picasso.Picasso


/**
 * Adapter for displaying products in the search results RecyclerView.
 * @param dataList: List of products to display.
 */
class SearchProductAdapter(var dataList: List<ProductListItem>) :
    RecyclerView.Adapter<SearchProductAdapter.DataViewHolder>() {

    /**
     * View holder for the RecyclerView.
     */
    class DataViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {}

    /**
     * Sets the filtered list of products and notifies the adapter of the change.
     */
    fun setFilteredList(mList: List<ProductListItem>){
        this.dataList = mList
        notifyDataSetChanged()
    }

    /**
     * Creates a view holder for the RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(inflater, parent, false)
        return DataViewHolder(binding)
    }

    /**
     * Binds data to the view holder.
     */
    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val data = dataList[position]

        data.let {
            holder.binding.apply {
                if (it.image.isNotEmpty()) {
                    Picasso.get().load(it.image).into(this.ivProduct)
                }
                this.productName.text = it.product_name
                this.productPrice.text = it.price.toString()
                this.productType.text = it.product_type
                this.productTag.text = it.tax.toString()
            }
        }
    }

    /**
     * Returns the number of items in the list.
     */
    override fun getItemCount(): Int {
        return dataList.size
    }
}
