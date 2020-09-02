package com.andysklyarov.finnotify.presentation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.andysklyarov.finnotify.R;

public class NamesListDialog extends AppCompatDialogFragment {

    private NamesListDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (NamesListDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "Must implement NamesListDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Activity activity = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AlertDialogCustom);

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_layout, null);

        builder.setView(view)
                .setTitle(R.string.dialog_title)
                .setItems(R.array.currencies, (dialogInterface, i) -> {
                    String code = getResources().getStringArray(R.array.currencies)[i];
                    listener.applyCode(getTargetRequestCode(), Activity.RESULT_OK, code);
                });

        AlertDialog dialog = builder.create();
        dialog.getWindow().setDimAmount(0.85f);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return dialog;
    }

    public interface NamesListDialogListener {
        void applyCode(int requestCode, int resultCode, String NameAndCode);
    }
}
