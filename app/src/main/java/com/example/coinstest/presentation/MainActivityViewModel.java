package com.example.coinstest.presentation;

import android.app.Application;

import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

import com.example.coinstest.domain.CurrencyInRub;
import com.example.coinstest.framework.receivers.AlarmServiceManager;
import com.example.coinstest.framework.receivers.ServiceState;
import com.example.coinstest.interactors.Interactors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class MainActivityViewModel extends AndroidViewModel {

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public final ObservableField<CurrencyInRub> currencyField = new ObservableField<>();
    public final ObservableField<Float> topLimit = new ObservableField<>();
    public final ObservableField<Float> bottomLimit = new ObservableField<>();
    public final ObservableField<Boolean> isServiceStarted = new ObservableField<>();

    private Interactors interactors;
    private AlarmServiceManager alarmManager = null;
    private LocalDateTime alarmTime;

    public MainActivityViewModel(Application application) {
        super(application);

        CurrencyInRub currency = new CurrencyInRub("USD", LocalDate.of(2010, 1, 1), 0.0f);
        currencyField.set(currency);
        this.interactors = null;

        alarmManager = new AlarmServiceManager(getApplication());

        ServiceState state = alarmManager.getServiceState();
        Date date = new Date(state.timeToStartInMillis);
        setAlarmTime(date.getHours(), date.getMinutes());
        topLimit.set(state.topLimit);
        bottomLimit.set(state.bottomLimit);
        isServiceStarted.set(state.isStarted);
    }

    public MainActivityViewModel(Application application, Interactors interactors) {
        super(application);

        CurrencyInRub currency = new CurrencyInRub("USD", LocalDate.of(2010, 1, 1), 0.0f);
        currencyField.set(currency);
        this.interactors = interactors;

        alarmManager = new AlarmServiceManager(getApplication());
        ServiceState state = alarmManager.getServiceState();
        Date date = new Date(state.timeToStartInMillis);
        setAlarmTime(date.getHours(), date.getMinutes());
        topLimit.set(state.topLimit);
        bottomLimit.set(state.bottomLimit);
        isServiceStarted.set(state.isStarted);
    }

    public void updateCurrencyInRub() {
        currencyField.set(interactors.getLastCurrency());
    }

    public void setAlarmTime(int hour, int minutes) {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.HOUR_OF_DAY, 0);

        Date currentTime = now.getTime();
        alarmTime = LocalDateTime.ofInstant(currentTime.toInstant(), ZoneId.systemDefault());
        alarmTime = alarmTime.plusHours(hour).plusMinutes(minutes);
    }

    public LocalDateTime getAlarmTime() {
        return alarmTime;
    }

    public void enableAlarm(float topLimit, float bottomLimit) {

        this.topLimit.set(topLimit);
        this.bottomLimit.set(bottomLimit);

        ZonedDateTime zdt = alarmTime.atZone(ZoneId.systemDefault());
        alarmManager.startRepeatingService(zdt, topLimit, bottomLimit);
    }

    public void disableAlarm() {
        alarmManager.stopRepeatingService();
    }

//    public void onResume() {
//        changeCurrencyInRub();
//    }

}


