package com.task.currencyconversion.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Error(
    val code: Int,
    val info: String
):Parcelable