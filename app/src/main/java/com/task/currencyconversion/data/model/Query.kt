package com.task.currencyconversion.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Query(
    val amount: Int,
    val from: String,
    val to: String
):Parcelable