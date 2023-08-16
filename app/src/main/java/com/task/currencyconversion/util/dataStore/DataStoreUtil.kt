package com.task.currencyconversion.util.dataStore

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userData")

class DataStoreUtil(context: Context) {

    private val dataStore = context.dataStore
    private val locality = stringPreferencesKey("locality")
    private val nightMode = intPreferencesKey("nightMode")
    private val fromCurrency = stringPreferencesKey("fromCurrency")
    private val toCurrency = stringPreferencesKey("toCurrency")

    suspend fun setLocality(language: String) {
        dataStore.edit { userData ->
            userData[locality] = language
        }
    }

    val localityFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[locality] ?: "en"
    }


    suspend fun setFromCurrency(currency: String) {
        dataStore.edit { userData ->
            userData[fromCurrency] = currency
        }
    }

    val fromCurrencyFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[fromCurrency] ?: ""
    }

    suspend fun setToCurrency(currency: String) {
        dataStore.edit { userData ->
            userData[toCurrency] = currency
        }
    }

    val toCurrencyFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[toCurrency] ?: ""
    }

    suspend fun setNightMode(mode: Int) {
        dataStore.edit { userData ->
            userData[nightMode] = mode
        }
    }

    val nightModeFlow: Flow<Int> = dataStore.data.map { preferences ->
        preferences[nightMode] ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }

}