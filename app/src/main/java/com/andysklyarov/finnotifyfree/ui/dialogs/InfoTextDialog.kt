package com.andysklyarov.finnotifyfree.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import com.andysklyarov.finnotifyfree.R

class InfoTextDialog : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity, R.style.AlertDialogCustom)

        val inflater = activity?.layoutInflater
        val view = inflater?.inflate(R.layout.info_dialog_layout, null)

        builder.setView(view)
            .setTitle(R.string.info_dialog_title)
            .setCancelable(false)
            .setPositiveButton("OK") { _, _ ->
            }

        val dialog = builder.create()
        dialog.window?.setDimAmount(resources.getFloat(R.dimen.dialog_dim_amount))
        dialog.window?.setBackgroundDrawableResource(R.color.dialog_background)

        return dialog
    }
}