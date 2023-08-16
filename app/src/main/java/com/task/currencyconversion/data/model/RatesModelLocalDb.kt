package com.task.currencyconversion.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(
    primaryKeys = ["currencyCode", "timestamp"]
)
@Parcelize
data class RatesModelLocalDb(
    val currencyCode: String, val exchangeRate: Double, val timestamp: Long
) : Parcelable