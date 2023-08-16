package com.task.currencyconversion.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SymbolsRes(
    val success: Boolean,
    val symbols: Map<String,String>
):Parcelable