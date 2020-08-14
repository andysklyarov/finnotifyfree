package com.example.coinstest.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.coinstest.R;
import com.example.coinstest.databinding.CurrencyFragmentBinding;
import com.example.coinstest.framework.ApplicationViewModelFactory;
import com.google.android.material.button.MaterialButton;

public class CurrencyFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private MainActivityViewModel viewModel;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static CurrencyFragment newInstance() {
        return new CurrencyFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentActivity activity = requireActivity();
        viewModel = new ViewModelProvider(activity,
                new ApplicationViewModelFactory(activity.getApplication())).
                get(MainActivityViewModel.class);

        CurrencyFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.currency_fragment, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(activity);

        //viewModel.changeCurrencyInRub();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        swipeRefreshLayout = view.findViewById(R.id.refresher);
        swipeRefreshLayout.setOnRefreshListener(this);

        MaterialButton settingsButton = view.findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(view1 -> {
            NavigationHost navigationHost = (NavigationHost) getActivity();
            if (navigationHost != null) {
                // Navigate to the next Fragment
                navigationHost.navigateTo(new SettingsFragment(), true);
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public void onResume() {
        super.onResume();
//        viewModel.onResume();
    }

    @Override
    public void onRefresh() {

        swipeRefreshLayout.post(()->{
            viewModel.updateCurrencyInRub();

            if (swipeRefreshLayout.isRefreshing())
                swipeRefreshLayout.setRefreshing(false);
        });
    }
}
