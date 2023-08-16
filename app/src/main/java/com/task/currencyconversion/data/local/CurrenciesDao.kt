package com.task.currencyconversion.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.task.currencyconversion.data.model.Currency
import com.task.currencyconversion.data.model.RatesModelLocalDb

@Dao
interface CurrenciesDao {
    @Query("select * from RatesModelLocalDb group by currencyCode limit 10")
    fun getRates(): LiveData<List<RatesModelLocalDb>>

    @Query("select * from RatesModelLocalDb where currencyCode=:currencyCode")
    fun getRatesByCurrencyCode(currencyCode: String): LiveData<List<RatesModelLocalDb>>

    @Query("delete from RatesModelLocalDb where timestamp<:timestamp")
    fun deleteRatesOldData(timestamp: Long)

    @Query("select * from RatesModelLocalDb where currencyCode=:currencyCode and timestamp>=:timestamp")
    fun getRateExchange(timestamp: Long, currencyCode: String): RatesModelLocalDb

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRate(vararg ratesModelLocalDb: RatesModelLocalDb)

    @Query("select * from Currency")
    fun getCurrencies(): LiveData<List<Currency>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrency(vararg currency: Currency)
}