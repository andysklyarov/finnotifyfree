package com.example.coinstest.presentation;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.coinstest.R;
import com.example.coinstest.databinding.ActivityMainBinding;
import com.example.coinstest.framework.ApplicationViewModelFactory;
import com.example.coinstest.framework.service.AlarmService;

public class MainActivity extends AppCompatActivity {
    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this, new ApplicationViewModelFactory()).get(MainActivityViewModel.class);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        startService(new Intent(this, AlarmService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.onResume();
    }
}



