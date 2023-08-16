package com.task.currencyconversion.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.task.currencyconversion.data.RepoOperations
import com.task.currencyconversion.data.model.RatesModelLocalDb
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repo: RepoOperations) : ViewModel() {
    private val _fromCurrencyCode = MutableLiveData<String>()
    private val _toCurrencyCode = MutableLiveData<String>()
    private var _currencyRatesList = _fromCurrencyCode.switchMap {
        repo.getRates(it)
    }
    val currencyRatesList: LiveData<List<RatesModelLocalDb>>
        get() = _currencyRatesList
    private var _currencyHistoricalDataList = _fromCurrencyCode.switchMap {
        repo.getRatesByCurrencyCode(it,_toCurrencyCode)
    }
    val currencyHistoricalDataList: LiveData<List<RatesModelLocalDb>>
        get() = _currencyHistoricalDataList

    fun setCurrenciesCodes(from:String,to:String){
        _fromCurrencyCode.value=from
        _toCurrencyCode.value=to
    }
}