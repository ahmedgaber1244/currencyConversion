package com.task.currencyconversion.util.helper

import android.text.InputFilter
import android.text.Spanned

class NumericInputFilter : InputFilter {
    private val regex = Regex("^[0-9]*\\.?[0-9]*$")

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val newValue = dest.toString().substring(0, dstart) + source?.subSequence(start, end) +
                dest.toString().substring(dend)

        return if (regex.matches(newValue)) {
            null
        } else {
            ""
        }
    }
}
