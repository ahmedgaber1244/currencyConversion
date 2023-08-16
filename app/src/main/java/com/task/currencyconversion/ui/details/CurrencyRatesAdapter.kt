package com.task.currencyconversion.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.task.currencyconversion.data.model.RatesModelLocalDb
import com.task.currencyconversion.databinding.CurrencyHistoricalDataItemBinding
import com.task.currencyconversion.databinding.CurrencyHistoricalDataItemBinding.inflate
import com.task.currencyconversion.util.helper.CurrencyDiffCallback

class CurrencyRatesAdapter : ListAdapter<RatesModelLocalDb, CurrencyRatesAdapter.ViewHolder>(
    CurrencyDiffCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder private constructor(private val binding: CurrencyHistoricalDataItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RatesModelLocalDb) {
            binding.data = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

