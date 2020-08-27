package com.andysklyarov.finnotify.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.andysklyarov.finnotify.R;
import com.andysklyarov.finnotify.databinding.SettingsFragmentBinding;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class SettingsFragment extends Fragment {
    private MainActivityViewModel viewModel = null;
    private boolean isSettingsSave = false;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final FragmentActivity activity = requireActivity();
        viewModel = new ViewModelProvider(activity).get(MainActivityViewModel.class);

        SettingsFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.settings_fragment, container, false);
        binding.setViewModel(viewModel);
        view = binding.getRoot();

        final TimePicker timePicker = view.findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
        timePicker.setHour(viewModel.getAlarmTime().getHour());
        timePicker.setMinute(viewModel.getAlarmTime().getMinute());

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                viewModel.setAlarmTime(timePicker.getHour(), timePicker.getMinute());
            }
        });

        Switch alarmSwitch = view.findViewById(R.id.alarm_switch);
        setSettingsVisibility(alarmSwitch.isChecked());

        alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                setSettingsVisibility(isChecked);
                isSettingsSave = isChecked;
            }
        });

        final Button backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSettingsSave) {

                    if (viewModel.isServiceStarted.get())
                        viewModel.disableAlarm();

                    TextInputEditText textInputTopLimit = view.findViewById(R.id.textInput_top_limit);
                    TextInputEditText textInputBottomLimit = view.findViewById(R.id.textInput_bottom_limit);

                    float topLimit = Float.parseFloat(textInputTopLimit.getText().toString());
                    float bottomLimit = Float.parseFloat(textInputBottomLimit.getText().toString());

                    if (topLimit < 0) topLimit = 0;
                    if (bottomLimit < 0) bottomLimit = 0;

                    viewModel.enableAlarm(topLimit, bottomLimit);
                } else {
                    viewModel.disableAlarm();
                }

                activity.getSupportFragmentManager().popBackStack();
            }
        });
        return view;
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
