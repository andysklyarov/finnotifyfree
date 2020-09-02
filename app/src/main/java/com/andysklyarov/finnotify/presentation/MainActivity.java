package com.andysklyarov.finnotify.presentation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.andysklyarov.finnotify.R;

public class MainActivity extends AppCompatActivity implements NavigationHost {
    private static final String SAVED_CODE_SHARED_PREFERENCES_KEY = "SAVED_CODE_SHARED_PREFERENCES_KEY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        String code = loadCode();
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container,  CurrencyFragment.newInstance(code))
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void navigateTo(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction transaction = getSupportFragmentManager().
                beginTransaction().
                replace(R.id.container, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }

    public String loadCode() {
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return myPreferences.getString(SAVED_CODE_SHARED_PREFERENCES_KEY, getString(R.string.default_currency_code));
    }

    public void safeCode(String code) {
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor myEditor = myPreferences.edit();
        myEditor.putString(SAVED_CODE_SHARED_PREFERENCES_KEY, code);
        myEditor.apply();
    }
}