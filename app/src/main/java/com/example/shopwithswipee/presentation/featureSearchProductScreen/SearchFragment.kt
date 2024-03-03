package com.example.shopwithswipee.presentation.featureSearchProductScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopwithswipee.databinding.FragmentSearchBinding
import com.example.shopwithswipee.datamodels.ProductListItem
import com.example.shopwithswipee.presentation.coreBase.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    private var productList : List<ProductListItem>? = null
    private lateinit var adapter: SearchProductAdapter
    private val viewModel: SearchViewModel by viewModels()

    override val customBindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSearchBinding
        get() = FragmentSearchBinding::inflate

    override fun setupAppBar() { }

    override fun setup(savedInstanceState: Bundle?) {
        initObservers()
        initClickListeners()
    }

    private fun initObservers() {
        viewModel.getProductList.observe(viewLifecycleOwner) {
            productList = it
            setupRecyclerView()
        }
    }

    private fun initClickListeners() {
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })
    }

    private fun setupRecyclerView() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
        adapter = SearchProductAdapter(productList!!)
        binding.recyclerView.adapter = adapter
    }

    /**
     * Filters the original list of Data objects based on the provided query.
     * @param query: The search query to filter the list.
     * If the query is not null, it iterates through the original list and adds items whose names contain the query (case-insensitive) to a new filtered list.
     * If the filtered list is not empty, it updates the adapter with the filtered data; otherwise, no data is displayed.
     */
    private fun filterList(query: String?) {

        if (query != null) {
            val filteredList = ArrayList<ProductListItem>()
            for (i in productList!!) {
                if (i.product_name.lowercase(Locale.ROOT).contains(query) || i.product_name.uppercase(Locale.ROOT).contains(query)) {
                    filteredList.add(i)
                }
            }
            adapter.setFilteredList(filteredList)
        }
    }


}