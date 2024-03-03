package com.example.shopwithswipee.presentation.coreBase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

/**
 * Abstract class representing a base fragment with view binding.
 * @param VB: The type of view binding for the fragment.
 */
abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    // View binding instance
    private var _binding: VB? = null
    protected val binding: VB
        get() = requireNotNull(_binding)

    // Abstract property to provide a custom binding inflater
    abstract val customBindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    // Coroutine related properties
    private var job: Job? = null
    protected lateinit var uiScope: CoroutineScope

    /**
     * Abstract method to set up the app bar.
     */
    abstract fun setupAppBar()

    /**
     * Called to have the fragment instantiate its user interface view.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = customBindingInflater.invoke(inflater, container, false)
        return binding.root
    }

    /**
     * Called immediately after onCreateView().
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        job = Job()
        uiScope = CoroutineScope(Dispatchers.Main.immediate + job!!)
        setupAppBar()
        setup(savedInstanceState)
    }

    /**
     * Abstract method to set up the fragment.
     */
    abstract fun setup(savedInstanceState: Bundle?)

    /**
     * Called when the view previously created by onCreateView() has been detached from the fragment.
     */
    override fun onDestroyView() {
        job?.cancel()
        _binding = null
        super.onDestroyView()
    }

    /**
     * Checks if the view binding is initialized.
     */
    protected fun isBindingInitialized() = _binding != null
}
