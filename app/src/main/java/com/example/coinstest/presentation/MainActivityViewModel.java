package com.example.coinstest.presentation;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

import com.example.coinstest.domain.CurrencyInRub;
import com.example.coinstest.framework.receivers.AlarmServiceManager;
import com.example.coinstest.interactors.Interactors;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

public class MainActivityViewModel extends AndroidViewModel {

    public final ObservableField<CurrencyInRub> currencyField = new ObservableField<>();
    private Interactors interactors;
    private AlarmServiceManager alarmManager = null;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        CurrencyInRub currency = new CurrencyInRub("Empty", "Empty", 0.0f);
        currencyField.set(currency);
        this.interactors = null;

        alarmManager = new AlarmServiceManager(getApplication());
    }

    public MainActivityViewModel(@NonNull Application application, Interactors interactors) {
        super(application);

        CurrencyInRub currency = new CurrencyInRub("Empty", "Empty", 0.0f);
        currencyField.set(currency);
        this.interactors = interactors;

        alarmManager = new AlarmServiceManager(getApplication());
    }

    public void changeCurrencyInRub() {
        currencyField.set(interactors.getLastCurrency());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void enableAlarm() {

        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.HOUR_OF_DAY, 0);

        Date currentTime = now.getTime();
        LocalDateTime ldt = LocalDateTime.ofInstant(currentTime.toInstant(), ZoneId.systemDefault());

        ldt = ldt.plusHours(22);

        ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());

        alarmManager.startRepeatingService(zdt);
    }

    public void disableAlarm(){
        alarmManager.stopRepeatingService();
    }

    public void onResume() {
        changeCurrencyInRub();
    }
}


