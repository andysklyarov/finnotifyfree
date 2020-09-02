package com.andysklyarov.finnotify.presentation;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

public class CurrencyFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, NamesListDialog.NamesListDialogListener {
    private static final String SAVED_CODE_KEY = "SAVED_CODE_KEY";
    public static final int DIALOG_FRAGMENT = 1;

    private MainViewModel viewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String currencyCode;

    public static CurrencyFragment newInstance(String currencyCode) {
        Bundle args = new Bundle();
        args.putString(SAVED_CODE_KEY, currencyCode);
        CurrencyFragment fragment = new CurrencyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        FragmentActivity activity = requireActivity();
        viewModel = new ViewModelProvider(activity,
                new ApplicationViewModelFactory(activity.getApplication())).
                get(MainViewModel.class);
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

        TextView textView = view.findViewById(R.id.currency_name);
        textView.setOnClickListener(v -> {
            openDialog();
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            currencyCode = args.getString(SAVED_CODE_KEY);
        } else {
            currencyCode = getString(R.string.default_currency_code);
        }

        viewModel.updateData(currencyCode);
    }

    private void openDialog() {
        FragmentActivity activity = getActivity();
        if (activity == null)
            return;

        NamesListDialog namesListDialog = new NamesListDialog();
        namesListDialog.setTargetFragment(this, DIALOG_FRAGMENT);
        namesListDialog.show(activity.getSupportFragmentManager(), "names dialog");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.post(() -> {
            viewModel.updateData(currencyCode);
        });
    }

    @Override
    public void applyCode(int requestCode, int resultCode, String NameAndCode) {
        if (requestCode == DIALOG_FRAGMENT) {
            if (resultCode == Activity.RESULT_OK) {
                currencyCode = NameAndCode.substring(NameAndCode.indexOf("/") + 1).trim();
                onRefresh();
                ((MainActivity)getActivity()).safeCode(currencyCode);

                Bundle args = new Bundle();
                args.putString(SAVED_CODE_KEY, currencyCode);
                setArguments(args);

                viewModel.disableAlarm();

            } else {
                Toast.makeText(getContext(), "Error!!!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
