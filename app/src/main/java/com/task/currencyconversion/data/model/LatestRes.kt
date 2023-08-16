package com.task.currencyconversion.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LatestRes(
    val base: String,
    val date: String,
    val rates: Map<String, Double>,
    val success: Boolean,
    val timestamp: Long
):Parcelable