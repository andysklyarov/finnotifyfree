package com.andysklyarov.finnotify.presentation;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.andysklyarov.finnotify.R;
import com.andysklyarov.finnotify.databinding.SettingsFragmentBinding;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class SettingsFragment extends Fragment {
    private MainViewModel viewModel = null;
    private boolean isSettingsSave = false;
    private View view;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        FragmentActivity activity = requireActivity();
        viewModel = new ViewModelProvider(activity).get(MainViewModel.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SettingsFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.settings_fragment, container, false);
        binding.setViewModel(viewModel);
        view = binding.getRoot();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initTimePicker();
        initAlarmSwitch();
        initBackButton();
    }

    private void initTimePicker() {
        final TimePicker timePicker = view.findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
        timePicker.setHour(viewModel.getAlarmTime().getHour());
        timePicker.setMinute(viewModel.getAlarmTime().getMinute());
    }

    private void initAlarmSwitch() {
        SwitchCompat alarmSwitch = view.findViewById(R.id.alarm_switch);

        isSettingsSave = viewModel.isServiceStarted.get();

        alarmSwitch.setChecked(isSettingsSave);
        setSettingsVisibility(alarmSwitch.isChecked());

        alarmSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            setSettingsVisibility(isChecked);
            isSettingsSave = isChecked;
        });
    }

    private void initBackButton() {
        Button backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            if (isSettingsSave) {

                if (viewModel.isServiceStarted.get())
                    viewModel.disableAlarm();

                TextInputEditText textInputTopLimit = view.findViewById(R.id.textInput_top_limit);
                TextInputEditText textInputBottomLimit = view.findViewById(R.id.textInput_bottom_limit);

                float topLimit = Float.parseFloat(textInputTopLimit.getText().toString());
                float bottomLimit = Float.parseFloat(textInputBottomLimit.getText().toString());

                TimePicker timePicker = view.findViewById(R.id.time_picker);

                if (topLimit < 0) topLimit = 0;
                if (bottomLimit < 0) bottomLimit = 0;

                int hours = timePicker.getHour();
                int minutes = timePicker.getMinute();

                viewModel.enableAlarm(hours, minutes, topLimit, bottomLimit);
            } else {
                viewModel.disableAlarm();
            }

            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void setSettingsVisibility(boolean isVisible) {
        int visibility;
        ArrayList<View> views = new ArrayList<>();

        views.add(view.findViewById(R.id.time_picker));

        views.add(view.findViewById(R.id.image_separator_1));
        views.add(view.findViewById(R.id.image_separator_2));
        views.add(view.findViewById(R.id.image_separator_3));
        views.add(view.findViewById(R.id.image_separator_4));

        views.add(view.findViewById(R.id.text_top_limit));
        views.add(view.findViewById(R.id.textInput_top_limit));

        views.add(view.findViewById(R.id.text_bottom_limit));
        views.add(view.findViewById(R.id.textInput_bottom_limit));

        if (isVisible) {
            visibility = View.VISIBLE;
        } else {
            visibility = View.INVISIBLE;
        }

        for (View v : views) {
            v.setVisibility(visibility);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
