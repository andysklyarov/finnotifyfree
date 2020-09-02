package com.andysklyarov.finnotify.presentation;

import androidx.annotation.DrawableRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.BindingAdapter;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class BindAdapters {
    @BindingAdapter("app:background")
    public static void loadImage(SwipeRefreshLayout layout, @DrawableRes int resource) {
        layout.setBackgroundResource(resource);
    }

    @BindingAdapter("app:background")
    public static void loadImage(ConstraintLayout layout, @DrawableRes int resource) {
        layout.setBackgroundResource(resource);
    }
}