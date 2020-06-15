package com.example.coinstest.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.coinstest.R;
import com.example.coinstest.databinding.CurrencyFragmentBinding;
import com.example.coinstest.framework.ApplicationViewModelFactory;
import com.google.android.material.button.MaterialButton;

public class CurrencyFragment extends Fragment {
    private MainActivityViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.currency_fragment, container, false);

        FragmentActivity activity = requireActivity();
        viewModel = new ViewModelProvider(activity,
                new ApplicationViewModelFactory(activity.getApplication())).
                get(MainActivityViewModel.class);


        CurrencyFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.currency_fragment, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(activity);

        viewModel.changeCurrencyInRub();
        View view = binding.getRoot();

        MaterialButton settingsButton = view.findViewById(R.id.settings_button);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationHost navigationHost = (NavigationHost) getActivity();
                if (navigationHost != null) {
                    // Navigate to the next Fragment
                    navigationHost.navigateTo(new SettingsFragment(), true);
                }
            }
        });

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.onResume();
    }
}
