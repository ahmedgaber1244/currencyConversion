package com.task.currencyconversion.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.currencyconversion.data.RepoOperations
import com.task.currencyconversion.util.apiStatus.RequestStatus
import com.task.currencyconversion.util.dataStore.DataStoreUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BaseViewModel @Inject constructor(
    private val repo: RepoOperations, private val dataStoreUtil: DataStoreUtil
) : ViewModel() {
    private val _locality = MutableStateFlow("")
    val locality: StateFlow<String> = _locality.asStateFlow()
    private val _nightMode = MutableStateFlow(0)
    val nightMode: StateFlow<Int> = _nightMode.asStateFlow()
    private val _latestRes = MutableStateFlow<RequestStatus<Any?>?>(null)
    val latestRes: StateFlow<RequestStatus<Any?>?> = _latestRes.asStateFlow()

    init {
        getLocality()
        getNightMode()
        updateData()
    }

    private fun updateData() {
        viewModelScope.launch {
            _latestRes.value = repo.ratesUpToDate()
        }
    }

    private fun getLocality() {
        viewModelScope.launch {
            dataStoreUtil.localityFlow.collect {
                _locality.value = it
            }
        }
    }

    private fun getNightMode() {
        viewModelScope.launch {
            dataStoreUtil.nightModeFlow.collect {
                _nightMode.value = it
            }
        }
    }
}