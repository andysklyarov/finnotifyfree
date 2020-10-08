package com.andysklyarov.finnotifyfree.ui.fragments

import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.andysklyarov.finnotifyfree.R

@BindingAdapter("app:background")
fun loadImage(layout: LinearLayout, @DrawableRes resource: Int) {
    layout.setBackgroundResource(resource)
}

@BindingAdapter("app:date_text")
fun TextView.setDateText(date: String?) {
    this.text =
        if (date.isNullOrEmpty()) "" else "${this.context.getString(R.string.update_on)} $date"
}

@BindingAdapter("app:nom_text", "app:code_text")
fun TextView.setNomText(nom: Int?, chCode: String?) {

    this.text = if (nom == null || nom < 0 || chCode.isNullOrEmpty()) {
        ""
    } else {
        "${this.context.getString(R.string.nom_string)} $nom $chCode"
    }
}

@BindingAdapter("app:text")
fun TextView.setTextFromFloat(value: Float?) {
    this.text = if (value != null) {
        "%.2f".format(value)
    } else {
        "0.00"
    }
}


@BindingAdapter("app:diff_text")
fun TextView.setDiffTextFromFloat(diffValue: Float?) {
    this.text = if (diffValue != null) {
        if (diffValue > 0) {
            "+%.2f".format(diffValue)
        } else {
            "%.2f".format(diffValue)
        }
    } else {
        "0.00"
    }
}