package com.example.shopwithswipee.presentation.featureHomeScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopwithswipee.R
import com.example.shopwithswipee.databinding.FragmentFeatureHomeScreenBinding
import com.example.shopwithswipee.datamodels.ProductListItem
import com.example.shopwithswipee.presentation.coreBase.BaseFragment
import com.example.shopwithswipee.presentation.featureAddProductScreen.AddProductScreen
import com.example.shopwithswipee.presentation.featureAddProductScreen.dismissCallback
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment representing the feature home screen.
 */
@AndroidEntryPoint
class FeatureHomeScreen : BaseFragment<FragmentFeatureHomeScreenBinding>() {

    // ViewModel instance
    private val viewModel: HomeViewModel by viewModels()

    // Adapter for displaying products
    private var adapter: ProductAdapter? = null

    /**
     * Provides the custom binding inflater for inflating the layout.
     */
    override val customBindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFeatureHomeScreenBinding
        get() = FragmentFeatureHomeScreenBinding::inflate

    /**
     * Sets up the app bar.
     */
    override fun setupAppBar() {}

    /**
     * Sets up the fragment.
     */
    override fun setup(savedInstanceState: Bundle?) {
        initObservers()
        initClickListeners()
        setUpBackPress()
        viewModel.getProductListData()
    }

    /**
     * Initializes click listeners.
     */
    private fun initClickListeners() {
        binding.ivSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeScreen_to_searchScreen)
        }

        binding.ivNotification.setOnClickListener {
            val addProductScreen = AddProductScreen(object : dismissCallback {
                override fun onFragmentDismissed(dismissed: Boolean) {
                    if(dismissed) viewModel.getProductListData()
                }
            })
            addProductScreen.show(parentFragmentManager,"Dialog",)
        }
    }

    /**
     * Initializes observers.
     */
    private fun initObservers() {
        viewModel.getProductList.observe(viewLifecycleOwner) {
            setupRecyclerView(it)
        }
        viewModel.showProgress.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = if(it) View.VISIBLE else View.GONE
        }
    }

    /**
     * Sets up the RecyclerView to display the list of products.
     * @param data: The list of products to be displayed.
     */
    private fun setupRecyclerView(data: PagingData<ProductListItem>) {
        adapter = ProductAdapter(object: ItemClickListener {
            override fun onItemClick(item: ProductListItem) { }
        })
        binding.rvHomeList.layoutManager = GridLayoutManager(context,2)
        adapter?.submitData(lifecycle,data)
        binding.rvHomeList.adapter = adapter
    }

    /**
     * Sets up the back press functionality to display exit confirmation.
     */
    private fun setUpBackPress() {
        val snackBarExitConfirmation = Snackbar.make(
            requireActivity().window.decorView.rootView,
            "Please press BACK again to exit",
            Snackbar.LENGTH_LONG
        )

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!snackBarExitConfirmation.isShown) {
                    snackBarExitConfirmation.show()
                } else {
                    requireActivity().finish()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}
