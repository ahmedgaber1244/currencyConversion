package com.task.currencyconversion.util.helper

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.squareup.moshi.Moshi
import com.task.currencyconversion.data.model.ErrorRes
import com.task.currencyconversion.data.model.RatesModelLocalDb
import com.task.currencyconversion.util.helper.Constants.debounceTimer
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.debounce
import okhttp3.ResponseBody
import java.util.Calendar
import java.util.Date
import kotlin.math.round


fun convertErrorBody(errorBody: ResponseBody?): ErrorRes? {
    return try {
        errorBody?.source()?.let {
            val moshiAdapter = Moshi.Builder().build().adapter(ErrorRes::class.java)
            moshiAdapter.fromJson(it)
        }
    } catch (exception: Exception) {
        null
    }
}

fun convertCurrency(from: RatesModelLocalDb, to: RatesModelLocalDb, amount: Double): Double {
    val value = (amount * to.exchangeRate) / from.exchangeRate
    return round(value * 10) / 10
}

fun getStartOfDayInMillis(days: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    calendar.add(Calendar.DAY_OF_MONTH, days)
    return calendar.timeInMillis / 1000
}


@OptIn(FlowPreview::class)
fun EditText.textChangesFlow(): Flow<String> {
    return channelFlow {
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.toString()?.let { this@channelFlow.trySend(it).isSuccess }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        addTextChangedListener(textWatcher)

        awaitClose { removeTextChangedListener(textWatcher) }
    }.debounce(debounceTimer)
}

fun getTimeStampDayStart(timeStamp:Long): Long {
    val datetime = Date(timeStamp * 1000)
    val calendar = Calendar.getInstance()
    calendar.time = datetime
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis / 1000
}