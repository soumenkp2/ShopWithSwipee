package com.example.shopwithswipee

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.view.WindowManager
import com.example.shopwithswipee.databinding.ActivityMainBinding
import com.example.shopwithswipee.presentation.coreBase.BaseActivity
import com.example.shopwithswipee.utils.NetworkReceiver
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    @Inject
    lateinit var networkReceiver: NetworkReceiver

    override val customBindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

    override fun setup() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(networkReceiver)
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkReceiver, filter)
    }
}