package com.task.currencyconversion.ui.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.currencyconversion.R
import com.task.currencyconversion.util.dataStore.DataStoreUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreUtil: DataStoreUtil
) : ViewModel() {
    private val _nightMode = MutableStateFlow(0)
    val nightMode: StateFlow<Int> = _nightMode.asStateFlow()
    private val _changeLanguage = MutableStateFlow(false)
    val changeLanguage: StateFlow<Boolean> = _changeLanguage.asStateFlow()

    init {
        getNightMode()
    }

    private fun getNightMode() {
        viewModelScope.launch {
            dataStoreUtil.nightModeFlow.collect {
                _nightMode.value = it
            }
        }
    }

    fun setMode(itemId: Int) {
        viewModelScope.launch {
            when (itemId) {
                R.id.nightAuto -> {
                    dataStoreUtil.setNightMode(1)
                }

                R.id.nightOff -> {
                    dataStoreUtil.setNightMode(2)
                }

                R.id.nightOn -> {
                    dataStoreUtil.setNightMode(3)
                }
            }
        }
    }

    fun setLanguage(itemId: Int) {
        viewModelScope.launch {
            when (itemId) {
                R.id.en -> {
                    dataStoreUtil.setLocality("en")
                }

                R.id.ar -> {
                    dataStoreUtil.setLocality("ar")
                }
            }
            _changeLanguage.value=true
            _changeLanguage.value=false
        }
    }
}