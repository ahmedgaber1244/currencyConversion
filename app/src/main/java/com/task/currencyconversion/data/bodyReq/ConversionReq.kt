package com.task.currencyconversion.data.bodyReq

import android.os.Parcelable
import com.task.currencyconversion.data.model.RatesModelLocalDb
import kotlinx.parcelize.Parcelize

@Parcelize
data class ConversionReq(
    var from: RatesModelLocalDb? = null, var to: RatesModelLocalDb? = null, var amount: Double
) : Parcelable