package com.task.currencyconversion.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.task.currencyconversion.data.local.CurrenciesDao
import com.task.currencyconversion.data.local.CurrenciesDb
import com.task.currencyconversion.data.model.Currency
import com.task.currencyconversion.data.model.RatesModelLocalDb
import com.task.currencyconversion.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers.hasSize
import org.hamcrest.collection.IsIterableContainingInAnyOrder
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Calendar
import java.util.Date

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class DaoTest {
    @get:Rule
    var instantTaskExecutorRule= InstantTaskExecutorRule()

    private lateinit var database: CurrenciesDb
    private lateinit var currenciesDao: CurrenciesDao

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CurrenciesDb::class.java
        ).build()
        currenciesDao=database.dbOperations()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun testInsertAndRetrieveRates() = runBlocking {
        val rate = RatesModelLocalDb("USD", 30.9, 1692170763)
        currenciesDao.insertRate(rate)
        val retrievedRates = currenciesDao.getRates().getOrAwaitValue()
        MatcherAssert.assertThat(retrievedRates, IsIterableContainingInAnyOrder.containsInAnyOrder(rate))
    }

    @Test
    fun testInsertDuplicates() = runBlocking {
        val rate1 = RatesModelLocalDb("USD", 30.9, getTimeStampDayStart(1692144000))
        val rate2 = RatesModelLocalDb("USD", 30.9, getTimeStampDayStart(1692144001))
        val rate3 = RatesModelLocalDb("USD", 30.9, getTimeStampDayStart(1692144002))
        currenciesDao.insertRate(rate1)
        currenciesDao.insertRate(rate2)
        currenciesDao.insertRate(rate3)
        val retrievedRates = currenciesDao.getRatesByCurrencyCode("USD").getOrAwaitValue()
        MatcherAssert.assertThat(retrievedRates, hasSize(1))
    }

    @Test
    fun testInsertAndRetrieveCurrencies() = runBlocking {
        val currency = Currency("USD", "United Stated Dollar")
        currenciesDao.insertCurrency(currency)
        val retrievedCurrencies = currenciesDao.getCurrencies().getOrAwaitValue()
        MatcherAssert.assertThat(retrievedCurrencies, IsIterableContainingInAnyOrder.containsInAnyOrder(currency))
    }

    fun getTimeStampDayStart(timeStamp:Long): Long {
        val datetime = Date(timeStamp * 1000)
        val calendar = Calendar.getInstance()
        calendar.time = datetime
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis / 1000
    }
}