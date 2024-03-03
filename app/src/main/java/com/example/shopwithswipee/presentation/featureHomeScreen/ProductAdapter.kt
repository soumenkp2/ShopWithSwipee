package com.example.shopwithswipee.presentation.featureHomeScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shopwithswipee.R
import com.example.shopwithswipee.databinding.ItemProductBinding
import com.example.shopwithswipee.datamodels.ProductListItem
import com.squareup.picasso.Picasso

/**
 * Adapter for displaying products in a RecyclerView.
 * @param listener: Listener for item click events.
 */
class ProductAdapter(private val listener: ItemClickListener) : PagingDataAdapter<ProductListItem, ProductAdapter.ProductItemViewHolder>(
    ProductDiffCallback()
) {

    /**
     * Creates a view holder for the RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(inflater, parent, false)
        return ProductItemViewHolder(binding)
    }

    /**
     * Binds data to the view holder.
     */
    override fun onBindViewHolder(holder: ProductItemViewHolder, position: Int) {
        val item = getItem(position)

        item?.let {
            holder.binding.apply {
                if(it.image.isNotEmpty()) {
                    Picasso.get().load(it.image).into(this.ivProduct)
                } else {
                    this.ivProduct.setImageResource(R.drawable.cart)
                }
                this.productName.text = it.product_name
                this.productPrice.text = it.price.toString()
                this.productType.text = it.product_type
                this.productTag.text = it.tax.toString()
            }
        }
    }

    /**
     * View holder for the RecyclerView.
     */
    class ProductItemViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {}

    // DiffCallback to efficiently update items in the RecyclerView
    private class ProductDiffCallback : DiffUtil.ItemCallback<ProductListItem>() {
        override fun areItemsTheSame(oldItem: ProductListItem, newItem: ProductListItem): Boolean {
            return oldItem.product_name == newItem.product_name // Use a unique identifier for items
        }

        override fun areContentsTheSame(oldItem: ProductListItem, newItem: ProductListItem): Boolean {
            return oldItem == newItem // Check if the items have the same content
        }
    }
}

/**
 * Interface for handling item click events.
 */
interface ItemClickListener {
    fun onItemClick(item : ProductListItem)
}
