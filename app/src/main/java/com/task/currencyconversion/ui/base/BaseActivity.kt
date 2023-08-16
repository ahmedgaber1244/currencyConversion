package com.task.currencyconversion.ui.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.task.currencyconversion.R
import com.task.currencyconversion.data.model.ErrorRes
import com.task.currencyconversion.databinding.ActivityBaseBinding
import com.task.currencyconversion.util.apiStatus.RequestStatus
import com.task.currencyconversion.util.helper.ProgressOperations
import com.task.currencyconversion.util.snackbar.customSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class BaseActivity : AppCompatActivity(), ProgressOperations {
    companion object {
        lateinit var progress: ProgressOperations
    }

    private lateinit var viewModel: BaseViewModel
    private lateinit var binding: ActivityBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_base)
        viewModel = ViewModelProvider(this)[BaseViewModel::class.java]
        lifecycleScope.launch {
            viewModel.locality.collect {
                loadLocality(it)
            }
        }
        lifecycleScope.launch {
            viewModel.nightMode.collect {
                loadNightMode(it)
            }
        }
        lifecycleScope.launch {
            viewModel.latestRes.collect {
                when(it){
                    is RequestStatus.Loading -> {
                        progress.show()
                    }

                    is RequestStatus.Success -> {
                        progress.hide()
                    }

                    is RequestStatus.Failure -> {
                        progress.hide()
                        val errorRes = it.data as ErrorRes
                        Snackbar.make(binding.root, errorRes.error.info, Snackbar.LENGTH_SHORT).show()
                    }

                    is RequestStatus.NetworkLost -> {
                        progress.networkLost()
                    }

                    else -> {}
                }
            }
        }
        fullscreen()
        hideProgress()
        progress = this
    }

    private fun fullscreen() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = resources.getColor(android.R.color.transparent)
    }

    private fun loadLocality(s: String) {
        val config = resources.configuration
        val locale = Locale(s)
        Locale.setDefault(locale)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun loadNightMode(i: Int) {
        when (i) {
            2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            3 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    private fun showProgress() {
        binding.showLoading = true
    }

    private fun hideProgress() {
        binding.showLoading = false
    }

    private fun showNetworkLost() {
        hideProgress()
        customSnackBar(this,binding.root,resources.getString(R.string.networkLost),false)
    }


    override fun show() {
        showProgress()
    }

    override fun hide() {
        hideProgress()
    }

    override fun networkLost() {
        showNetworkLost()
    }
}