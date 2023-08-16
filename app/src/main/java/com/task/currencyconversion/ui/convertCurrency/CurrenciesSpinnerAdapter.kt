package com.task.currencyconversion.ui.convertCurrency

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import androidx.annotation.LayoutRes
import com.task.currencyconversion.data.model.Currency
import com.task.currencyconversion.databinding.SearchAutocompleteItemBinding

class CurrenciesSpinnerAdapter(
    context: Context, @LayoutRes private val layoutResource: Int, cities: List<Currency>
) : ArrayAdapter<Currency>(context, layoutResource, cities) {
    private val rates: MutableList<Currency> = ArrayList(cities)
    private var allRates: List<Currency> = ArrayList(cities)

    override fun getCount(): Int {
        return rates.size
    }

    override fun getItem(position: Int): Currency {
        return rates[position]
    }

    override fun getItemId(position: Int): Long {
        return rates[position].hashCode().toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val binding = SearchAutocompleteItemBinding.inflate(inflater, parent, false)
        try {
            val itemData: Currency = getItem(position)
            binding.data = itemData
            binding.executePendingBindings()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return binding.root
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun convertResultToString(resultValue: Any): String {
                val itemData = resultValue as Currency
                return itemData.code
            }

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint != null) {
                    val ratesSuggestion: MutableList<Currency> = ArrayList()
                    for (rateData in allRates) {
                        if (rateData.code.contains(
                                constraint.toString(),
                                true
                            ) or rateData.name.contains(
                                constraint.toString(), true
                            )
                        ) {
                            ratesSuggestion.add(rateData)
                        }
                    }
                    filterResults.values = ratesSuggestion
                    filterResults.count = ratesSuggestion.size
                }
                return filterResults
            }

            override fun publishResults(
                constraint: CharSequence?, results: FilterResults
            ) {
                rates.clear()
                if (results.count > 0) {
                    for (result in results.values as List<*>) {
                        if (result is Currency) {
                            rates.add(result)
                        }
                    }
                    notifyDataSetChanged()
                } else if (constraint == null) {
                    rates.addAll(allRates)
                    notifyDataSetInvalidated()
                }
            }
        }
    }
}