package com.task.currencyconversion.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ErrorRes(
    val error: Error,
    val success: Boolean
):Parcelable