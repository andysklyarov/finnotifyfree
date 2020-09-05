package com.andysklyarov.finnotifyfree.presentation

import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


@BindingAdapter("app:background")
fun loadImage(layout: SwipeRefreshLayout, @DrawableRes resource: Int) {
    layout.setBackgroundResource(resource)
}

@BindingAdapter("app:background")
fun loadImage(layout: ConstraintLayout, @DrawableRes resource: Int) {
    layout.setBackgroundResource(resource)
}