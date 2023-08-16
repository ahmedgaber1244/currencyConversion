package com.task.currencyconversion.util.helper

import com.task.currencyconversion.BuildConfig

object Constants {
    const val BASE_URL = "http://data.fixer.io/api/"
    val ApiKey: String = BuildConfig.ApiKey
    const val debounceTimer = 1000L
    const val timeFormat = "dd/MM/yyyy"

}