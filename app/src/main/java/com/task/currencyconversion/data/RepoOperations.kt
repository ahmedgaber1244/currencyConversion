package com.task.currencyconversion.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.task.currencyconversion.data.local.CurrenciesDao
import com.task.currencyconversion.data.model.Currency
import com.task.currencyconversion.data.model.ErrorRes
import com.task.currencyconversion.data.model.RatesModelLocalDb
import com.task.currencyconversion.data.remote.RemoteService
import com.task.currencyconversion.util.apiStatus.RequestStatus
import com.task.currencyconversion.util.helper.Constants
import com.task.currencyconversion.util.helper.convertCurrency
import com.task.currencyconversion.util.helper.convertErrorBody
import com.task.currencyconversion.util.helper.getStartOfDayInMillis
import com.task.currencyconversion.util.helper.getTimeStampDayStart
import javax.inject.Inject

class RepoOperations @Inject constructor(
    private val repository: RemoteService, private val dao: CurrenciesDao
) {
    private suspend fun latest() = repository.latest(Constants.ApiKey)
    private suspend fun symbols() = repository.symbols(Constants.ApiKey)
    fun getCurrencies() = dao.getCurrencies()
    private fun insertRate(vararg ratesModelLocalDb: RatesModelLocalDb) = dao.insertRate(*ratesModelLocalDb)
    private fun insertCurrency(vararg currency: Currency) = dao.insertCurrency(*currency)
    private fun onGetRates() = dao.getRates()
    private fun onGetRatesByCurrencyCode(currencyCode: String) =
        dao.getRatesByCurrencyCode(currencyCode)

    fun deleteRatesOldData(timestamp: Long) = dao.deleteRatesOldData(timestamp)

    suspend fun ratesUpToDate(): RequestStatus<Any?> {
        return try {
            val res = latest()
            if (res.isSuccessful) {
                res.body()?.let {
                    val map = it.rates.entries.map { entry ->
                        RatesModelLocalDb(
                            currencyCode = entry.key,
                            exchangeRate = entry.value,
                            timestamp = getTimeStampDayStart(it.timestamp)
                        )
                    }.toTypedArray()
                    insertRate(*map)
                }
                return RequestStatus.Success(res.body())
            } else {
                val errorResponse: ErrorRes? = convertErrorBody(res.errorBody())
                return RequestStatus.Failure(errorResponse)
            }
        } catch (e: Exception) {
            RequestStatus.NetworkLost()
        }
    }

    suspend fun updateSymbols(): RequestStatus<Any?> {
        return try {
            val res = symbols()
            if (res.isSuccessful) {
                res.body()?.let {
                    val map = it.symbols.entries.map { entry ->
                        Currency(entry.key, entry.value)
                    }
                    insertCurrency(*map.toTypedArray())
                }
                return RequestStatus.Success(res.body())
            } else {
                val errorResponse: ErrorRes? = convertErrorBody(res.errorBody())
                return RequestStatus.Failure(errorResponse)
            }
        } catch (e: Exception) {
            RequestStatus.NetworkLost()
        }
    }

    fun getRate(currency: String): RatesModelLocalDb {
        return dao.getRateExchange(getStartOfDayInMillis(0), currency)
    }

    fun getRatesByCurrencyCode(
        from: String, to: LiveData<String>
    ): LiveData<List<RatesModelLocalDb>> {
        return to.switchMap { s ->
            onGetRatesByCurrencyCode(s).map { ratesModelLocalDbs ->
                ratesModelLocalDbs.map { toRateExchange ->
                    transformRate(
                        from, toRateExchange
                    )
                }
            }
        }
    }

    fun getRates(from: String): LiveData<List<RatesModelLocalDb>> {
        return onGetRates().map { ratesModelLocalDbs ->
            ratesModelLocalDbs.map { toRateExchange ->
                transformRate(
                    from, toRateExchange
                )
            }
        }
    }

    private fun transformRate(from: String, toRateExchange: RatesModelLocalDb): RatesModelLocalDb {
        val fromRateExchange = dao.getRateExchange(toRateExchange.timestamp, from)
        val exchangeRate = convertCurrency(fromRateExchange, toRateExchange, 1.0)
        return RatesModelLocalDb(
            currencyCode = toRateExchange.currencyCode,
            exchangeRate = exchangeRate,
            timestamp = toRateExchange.timestamp
        )
    }
}