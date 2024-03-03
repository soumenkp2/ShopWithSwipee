package com.example.shopwithswipee.presentation.featureAddProductScreen

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.shopwithswipee.databinding.FragmentAddProductScreenBinding
import com.example.shopwithswipee.datamodels.AddProduct
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 * This class represents a screen for adding a new product.
 * It allows the user to input details such as product name, type, price, tax, and image.
 *
 * @property dismissCallback A callback interface for notifying when the fragment is dismissed.
 */
@AndroidEntryPoint
class AddProductScreen(private val dismissCallback: dismissCallback) : BottomSheetDialogFragment() {

    // Permission required to access external storage for image selection
    private val requiredPermission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_IMAGES
        else
            Manifest.permission.READ_EXTERNAL_STORAGE

    // View binding instance for the fragment
    private var binding: FragmentAddProductScreenBinding? = null

    // View model for handling product addition
    private val viewModel: AddProductViewModel by viewModels()

    // File representing the selected image
    private var imageFile: File? = null

    // Request code for picking an image from the gallery
    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddProductScreenBinding.inflate(inflater, container, false)
        return binding?.root
    }

    /**
     * Called immediately after the fragment's view is created.
     * Initializes UI components and sets up event listeners.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpinner()
        initObservers()
        initClickListeners()
    }

    /**
     * Sets up the product type spinner with options.
     */
    private fun setupSpinner() {
        val productTypes = arrayOf("Type 1", "Type 2", "Type 3")
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, productTypes)
        binding?.spinnerProductType?.adapter = spinnerAdapter
    }

    /**
     * Initializes observers for observing view model data changes.
     */
    private fun initObservers() {
        viewModel.addProductResponse.observe(viewLifecycleOwner) {
            if (it.success) {
                binding?.progressBar?.visibility = View.GONE
                dismissCallback.onFragmentDismissed(true)
                Toast.makeText(context, "Your product has been added", Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }
    }

    /**
     * Initializes click listeners for UI components.
     */
    private fun initClickListeners() {
        binding?.addImageProduct?.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), requiredPermission)
                == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {
                requestPermission.launch(arrayOf(requiredPermission))
            }
        }

        binding?.buttonSubmit?.setOnClickListener {
            if (allFieldsFilled()) {
                val productType = binding?.spinnerProductType?.selectedItem as String
                val sellingPrice = binding?.editTextSellingPrice?.text?.toString()?.toDouble() ?: 0.00
                val productName = binding?.editTextProductName?.text?.toString() ?: "ProductName"
                val taxRate = binding?.editTextTaxRate?.text?.toString()?.toDouble() ?: 0.00

                viewModel.submitProduct(
                    AddProduct(
                        productName = productName,
                        productType = productType,
                        price = sellingPrice,
                        tax = taxRate,
                        files = imageFile
                    )
                )

                binding?.progressBar?.visibility = View.VISIBLE

            } else {
                Snackbar.make(binding!!.root, "Fill all the fields", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    // Activity result launcher for requesting permissions
    private val requestPermission: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach { isGranted ->
                if (!isGranted.value) {
                    Toast.makeText(context, "Storage access denied.", Toast.LENGTH_LONG).show()
                }
            }
        }

    // Opens the gallery for selecting an image
    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
    }

    /**
     * Called when the result of an activity is received.
     * Handles the result of image selection from the gallery.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri: Uri? = data.data
            imageUri?.let {
                lifecycleScope.launch {
                    try {
                        imageFile = uriToImageFile(it)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                binding?.addImageProduct?.visibility = View.GONE
                binding?.imageProduct?.visibility = View.VISIBLE
                binding?.imageProduct?.setImageURI(it)
            }
        }
    }

    /**
     * Converts a URI to a File object.
     */
    private suspend fun uriToImageFile(uri: Uri): File =
        withContext(Dispatchers.IO) {
            var result: File? = null
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = requireContext().contentResolver.query(uri, filePathColumn, null, null, null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    val filePath = cursor.getString(columnIndex)
                    cursor.close()
                    result = File(filePath)
                }
                cursor.close()
            }
            return@withContext result!!
        }

    /**
     * Checks if all required fields are filled.
     */
    private fun allFieldsFilled(): Boolean {
        return with(binding) {
            !this?.editTextProductName?.text.isNullOrBlank() &&
                    !this?.editTextSellingPrice?.text.isNullOrBlank() &&
                    !this?.editTextTaxRate?.text.isNullOrBlank() && this?.addImageProduct?.visibility == View.GONE
        }
    }
}

/**
 * Interface for handling dismissal callback from the AddProductScreen.
 */
interface dismissCallback {
    /**
     * Called when the AddProductScreen is dismissed.
     * @param dismissed Indicates whether the screen was dismissed successfully.
     */
    fun onFragmentDismissed(dismissed: Boolean)
}
