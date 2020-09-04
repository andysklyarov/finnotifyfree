package com.andysklyarov.finnotify.presentation

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import com.andysklyarov.finnotify.R


class NamesListDialog : AppCompatDialogFragment() {

    private lateinit var listener: NamesListDialogListener


    override fun onAttach(context: Context) {
        super.onAttach(context)

        listener = try {
            (targetFragment as NamesListDialogListener)
        } catch (e: ClassCastException) {
            throw ClassCastException(
                context.toString().toString() +
                        "Must implement NamesListDialogListener"
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity, R.style.AlertDialogCustom)

        val inflater = activity?.layoutInflater
        val view = inflater?.inflate(R.layout.dialog_layout, null)

        builder.setView(view)
            .setTitle(R.string.dialog_title)
            .setItems(R.array.currencies){ _, i ->
                val code = resources.getStringArray(R.array.currencies)[i]
                listener.applyCode(targetRequestCode, Activity.RESULT_OK, code)
            }

        val dialog = builder.create()
        dialog.window?.setDimAmount(0.85f)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        return dialog;
    }

    interface NamesListDialogListener {
        fun applyCode(requestCode: Int, resultCode: Int, NameAndCode: String)
    }
}