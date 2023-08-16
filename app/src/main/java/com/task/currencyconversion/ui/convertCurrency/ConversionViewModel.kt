package com.task.currencyconversion.ui.convertCurrency

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.task.currencyconversion.R
import com.task.currencyconversion.data.RepoOperations
import com.task.currencyconversion.data.bodyReq.ConversionReq
import com.task.currencyconversion.data.model.Currency
import com.task.currencyconversion.data.model.RatesModelLocalDb
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ConversionViewModel @Inject constructor(private val repo: RepoOperations) : ViewModel() {
    val currenciesList: LiveData<List<Currency>>
        get() = repo.getCurrencies()
    private val _convertRes = MutableStateFlow<ConversionReq?>(null)
    val convertRes: StateFlow<ConversionReq?> = _convertRes.asStateFlow()
    private val _navToFragment = MutableStateFlow(0)
    val navToFragment: StateFlow<Int> = _navToFragment.asStateFlow()
    private val _validation = MutableStateFlow(0)
    val validation: StateFlow<Int> = _validation.asStateFlow()

    fun navToFragment(id: Int, req: ConversionReq) {
        when (id) {
            R.id.settings -> {
                _navToFragment.value = R.id.action_conversionFragment_to_settings
            }

            R.id.details -> navToDetails(req)
        }
    }

    private fun navToDetails(req: ConversionReq) {
        if (req.to != null && req.from != null) {
            _navToFragment.value = R.id.action_conversionFragment_to_detailsFragment
        } else {
            _validation.value = R.string.pleaseSelectCurrencies
        }
    }

    fun switchCurrency(req: ConversionReq) {
        if (req.to != null && req.from != null && req.amount > 0) {
            _convertRes.value = ConversionReq(from = req.to, to = req.from, amount = req.amount)
        }
    }

    fun getRateLocalModuleDb(currency: String): RatesModelLocalDb {
        return repo.getRate(currency)
    }

    fun resetNav(){
        _navToFragment.value = 0
    }
}