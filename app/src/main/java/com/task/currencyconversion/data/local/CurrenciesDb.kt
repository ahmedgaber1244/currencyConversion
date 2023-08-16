package com.task.currencyconversion.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.task.currencyconversion.data.model.Currency
import com.task.currencyconversion.data.model.RatesModelLocalDb

@Database(entities = [RatesModelLocalDb::class, Currency::class], version = 1)
abstract class CurrenciesDb : RoomDatabase() {
    abstract fun dbOperations(): CurrenciesDao
}