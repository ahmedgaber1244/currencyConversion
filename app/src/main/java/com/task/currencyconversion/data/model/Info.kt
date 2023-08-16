package com.task.currencyconversion.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Info(
    val rate: Double,
    val timestamp: Int
):Parcelable