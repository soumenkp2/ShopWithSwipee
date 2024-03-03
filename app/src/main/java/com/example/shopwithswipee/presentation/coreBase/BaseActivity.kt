package com.example.shopwithswipee.presentation.coreBase

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

/**
 * Abstract class representing a base activity with view binding.
 * @param VB: The type of view binding for the activity.
 */
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    // View binding instance
    private var _binding: VB? = null
    protected val binding: VB
        get() = requireNotNull(_binding)

    // Abstract property to provide a custom binding inflater
    abstract val customBindingInflater: (LayoutInflater) -> VB

    // Coroutine related properties
    private var job: Job? = null
    protected lateinit var uiScope: CoroutineScope

    // Toast instance
    private var toast: Toast? = null

    /**
     * Abstract method to set up the activity.
     */
    abstract fun setup()

    /**
     * Called when the activity is first created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
        uiScope = CoroutineScope(Dispatchers.Main.immediate + job!!)
        _binding = customBindingInflater.invoke(layoutInflater)
        setContentView(binding.root)
        setup()
    }

    /**
     * Called when the activity is about to be destroyed.
     */
    override fun onDestroy() {
        super.onDestroy()
        toast?.cancel()
        job?.cancel()
        _binding = null
    }
}

