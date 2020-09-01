package com.andysklyarov.finnotify.presentation;

import android.content.Context;
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

import com.andysklyarov.finnotify.R;
import com.andysklyarov.finnotify.databinding.CurrencyFragmentBinding;
import com.andysklyarov.finnotify.framework.ApplicationViewModelFactory;
import com.google.android.material.button.MaterialButton;

public class CurrencyFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private MainViewModel viewModel;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static CurrencyFragment newInstance() {
        return new CurrencyFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        FragmentActivity activity = requireActivity();
        viewModel = new ViewModelProvider(activity,
                new ApplicationViewModelFactory(activity.getApplication())).
                get(MainViewModel.class);

        viewModel.updateData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentActivity activity = requireActivity();

        CurrencyFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.currency_fragment, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(activity);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        swipeRefreshLayout = view.findViewById(R.id.refresher);
        swipeRefreshLayout.setOnRefreshListener(this);

        MaterialButton settingsButton = view.findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(navigationView -> {
            NavigationHost navigationHost = (NavigationHost) getActivity();
            if (navigationHost != null) {
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
    public void onRefresh() {
        swipeRefreshLayout.post(()->{
            viewModel.updateData();
        });
    }
}
