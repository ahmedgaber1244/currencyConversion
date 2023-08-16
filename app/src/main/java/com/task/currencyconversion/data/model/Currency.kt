package com.task.currencyconversion.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Currency(
    @PrimaryKey val code: String, val name: String
):Parcelable