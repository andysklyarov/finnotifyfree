package com.andysklyarov.finnotifyfree.ui

import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

@BindingAdapter("app:background")
fun loadImage(layout: LinearLayout, @DrawableRes resource: Int) {
    layout.setBackgroundResource(resource)
}