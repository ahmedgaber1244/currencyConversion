package com.task.currencyconversion.util.snackbar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import com.task.currencyconversion.databinding.NotificationBinding


@SuppressLint("RestrictedApi")
fun customSnackBar(context: Context, view: View, text: String, success: Boolean) {
    val snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG)
    val layoutInflater = LayoutInflater.from(context)
    val binding = NotificationBinding.inflate(layoutInflater)
    binding.data = text
    binding.success = success
    val customSnackView: View = binding.root
    snackbar.view.setBackgroundColor(Color.TRANSPARENT)
    val snackbarLayout = snackbar.view as SnackbarLayout
    snackbarLayout.setPadding(0, 50, 0, 0)
    snackbarLayout.addView(customSnackView, 0)
    val params = snackbar.view.layoutParams
    if (params is CoordinatorLayout.LayoutParams) {
        params.gravity = Gravity.BOTTOM
    } else {
        (params as FrameLayout.LayoutParams).gravity = Gravity.TOP
    }
    snackbar.view.layoutParams = params
    snackbar.show()
}