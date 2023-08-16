package com.task.currencyconversion.util.helper

import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.task.currencyconversion.R
import com.task.currencyconversion.data.bodyReq.ConversionReq
import com.task.currencyconversion.data.model.Currency
import com.task.currencyconversion.data.model.RatesModelLocalDb
import com.task.currencyconversion.ui.convertCurrency.CurrenciesSpinnerAdapter
import com.task.currencyconversion.util.helper.Constants.timeFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@BindingAdapter("setExchangeRateResult")
fun bindExchangeRateResult(textView: TextView, data: ConversionReq?) {
    data?.from?.let {
        val convertCurrency = convertCurrency(data.from!!, data.to!!, data.amount)
        textView.text=convertCurrency.toString()
    }
}

@BindingAdapter("setExchangeRate")
fun bindExchangeRate(textView: TextView, data: RatesModelLocalDb?) {
    data?.let {
        val currencyCode = data.currencyCode
        val exchangeRate = data.exchangeRate
        val txt = "$exchangeRate $currencyCode"
        textView.text = txt
    }
}

@BindingAdapter("setExchangeRateDate")
fun bindExchangeRateDate(textView: TextView, data: RatesModelLocalDb?) {
    data?.let {
        val date = Date(data.timestamp*1000)
        val dateFormat = SimpleDateFormat(timeFormat, Locale.getDefault())
        val formattedDate = dateFormat.format(date)
        textView.text = formattedDate
    }
}

@BindingAdapter("setCurrenciesAdapter")
fun bindCurrenciesAdapter(autoCompleteTextView: AutoCompleteTextView, data: List<Currency>?) {
    val adapter = data?.let {
        CurrenciesSpinnerAdapter(
            autoCompleteTextView.context, R.layout.search_autocomplete_item, data
        )
    }
    autoCompleteTextView.setAdapter(adapter)
}

@BindingAdapter("setCurrencyName")
fun bindCurrencyName(autoCompleteTextView: AutoCompleteTextView, data: RatesModelLocalDb?) {
    data?.let {
        autoCompleteTextView.setText(data.currencyCode,false)
    }
}

@BindingAdapter("setCurrencyHistoryChary")
fun bindCurrencyHistoryChary(lineChart: LineChart, data: List<RatesModelLocalDb>?) {
    data?.let {
        val entries = ArrayList<Entry>()
        data.forEach { ratesModelLocalDb ->
            entries.add(
                Entry(
                    ratesModelLocalDb.exchangeRate.toFloat(), ratesModelLocalDb.timestamp.toFloat()
                )
            )
        }
        val resources = lineChart.context.resources
        val dataSet = LineDataSet(entries, resources.getString(R.string.dataChart))
        val lineData = LineData(dataSet)
        lineChart.data = lineData
        dataSet.color = resources.getColor(R.color.amaranth_purple)
        dataSet.valueTextColor = resources.getColor(R.color.black)
        dataSet.setCircleColor(resources.getColor(R.color.purple_200))
        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(arrayOf("Label1", "Label2", "Label3"))
        lineChart.invalidate()
    }
}


