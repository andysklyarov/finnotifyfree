package com.andysklyarov.finnotify.presentation;

import android.app.Application;
import android.view.View;

import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

import com.andysklyarov.finnotify.R;
import com.andysklyarov.finnotify.domain.CurrencyInRub;
import com.andysklyarov.finnotify.framework.receivers.AlarmServiceManager;
import com.andysklyarov.finnotify.framework.receivers.ServiceState;
import com.andysklyarov.finnotify.interactors.Interactors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends AndroidViewModel {

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public final ObservableField<CurrencyInRub> nowCurrency = new ObservableField<>();
    public final ObservableField<Float> topLimit = new ObservableField<>();
    public final ObservableField<Float> bottomLimit = new ObservableField<>();
    public final ObservableField<Boolean> isServiceStarted = new ObservableField<>();

    public final ObservableField<Boolean> isLoading = new ObservableField<>();

    public final ObservableField<Integer> mainPartsVisibility = new ObservableField<>();
    public final ObservableField<Integer> errorVisibility = new ObservableField<>();

    private Interactors interactors;
    private AlarmServiceManager alarmManager = null;
    private LocalDateTime alarmTime;

    public MainViewModel(Application application) {
        super(application);

        CurrencyInRub currency = new CurrencyInRub("USD", LocalDate.of(0, 1, 1), 0.0f);
        nowCurrency.set(currency);
        this.interactors = null;

        alarmManager = new AlarmServiceManager(getApplication());
        ServiceState state = alarmManager.getServiceState();
        Date date = new Date(state.timeToStartInMillis);
        setAlarmTime(date.getHours(), date.getMinutes());
        topLimit.set(state.topLimit);
        bottomLimit.set(state.bottomLimit);
        isServiceStarted.set(state.isStarted);

        isLoading.set(false);
        mainPartsVisibility.set(View.GONE);
        errorVisibility.set(View.GONE);
    }

    public MainViewModel(Application application, Interactors interactors) {
        super(application);

        CurrencyInRub currency = new CurrencyInRub("USD", LocalDate.of(0, 1, 1), 0.0f);
        nowCurrency.set(currency);
        this.interactors = interactors;

        alarmManager = new AlarmServiceManager(getApplication());
        ServiceState state = alarmManager.getServiceState();
        Date date = new Date(state.timeToStartInMillis);
        setAlarmTime(date.getHours(), date.getMinutes());
        topLimit.set(state.topLimit);
        bottomLimit.set(state.bottomLimit);
        isServiceStarted.set(state.isStarted);

        isLoading.set(false);
        mainPartsVisibility.set(View.GONE);
        errorVisibility.set(View.GONE);
    }

    public void updateData() {

        Disposable res = interactors.getLastCurrency()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> isLoading.set(true))
                .doFinally(() -> isLoading.set(false))
                .subscribe(currencyInRub -> {
                    nowCurrency.set(currencyInRub);
                    mainPartsVisibility.set(View.VISIBLE);
                    errorVisibility.set(View.GONE);
                }, throwable -> {
                    mainPartsVisibility.set(View.GONE);
                    errorVisibility.set(View.VISIBLE);
                });
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

    public String getPreamble() {
        return getApplication().getResources().getString(R.string.update_on);
    }
}