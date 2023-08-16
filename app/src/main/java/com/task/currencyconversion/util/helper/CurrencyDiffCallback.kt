package com.task.currencyconversion.util.helper

import androidx.recyclerview.widget.DiffUtil
import com.task.currencyconversion.data.model.RatesModelLocalDb

class CurrencyDiffCallback : DiffUtil.ItemCallback<RatesModelLocalDb>() {
    override fun areItemsTheSame(oldItem: RatesModelLocalDb, newItem: RatesModelLocalDb): Boolean {
        return oldItem.timestamp == newItem.timestamp
    }

    override fun areContentsTheSame(
        oldItem: RatesModelLocalDb, newItem: RatesModelLocalDb
    ): Boolean {
        return oldItem == newItem
    }
}