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
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends AndroidViewModel {

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public final ObservableField<CurrencyInRub> nowCurrency = new ObservableField<>();
    public final ObservableField<String> diffCurrency = new ObservableField<>();

    public final ObservableField<Float> topLimit = new ObservableField<>();
    public final ObservableField<Float> bottomLimit = new ObservableField<>();

    public final ObservableField<Boolean> isServiceStarted = new ObservableField<>();
    public final ObservableField<Boolean> isLoading = new ObservableField<>();

    public final ObservableField<Integer> mainPartsVisibility = new ObservableField<>();
    public final ObservableField<Integer> errorVisibility = new ObservableField<>();

    private Interactors interactors;
    private AlarmServiceManager alarmManager;

    public MainViewModel(Application application) {
        super(application);
        initViewModel(null);
    }

    public MainViewModel(Application application, Interactors interactors) {
        super(application);
        initViewModel(interactors);
    }

    public void updateData() {

        Disposable res = interactors.getLastDiffCurrency()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> isLoading.set(true))
                .doFinally(() -> isLoading.set(false))
                .subscribe(diffCurrencyInRub -> {
                    nowCurrency.set(diffCurrencyInRub);

                    if (diffCurrencyInRub.diff > 0)
                        diffCurrency.set("+" + diffCurrencyInRub.diff);
                    else
                        diffCurrency.set(String.valueOf(diffCurrencyInRub.diff));

                    mainPartsVisibility.set(View.VISIBLE);
                    errorVisibility.set(View.GONE);
                }, throwable -> {
                    mainPartsVisibility.set(View.GONE);
                    errorVisibility.set(View.VISIBLE);
                });
    }

    public void enableAlarm(int hour, int minutes, float topLimit, float bottomLimit) {
        this.topLimit.set(topLimit);
        this.bottomLimit.set(bottomLimit);

        alarmManager.setAlarmTime24(hour, minutes);
        alarmManager.startRepeatingService(topLimit, bottomLimit);
        updateAlarmState();
    }

    public void disableAlarm() {
        alarmManager.stopRepeatingService();
        updateAlarmState();
    }

    public ZonedDateTime getAlarmTime() {
        return alarmManager.getAlarmTime();
    }

    public String getDatePreamble() {
        return getApplication().getResources().getString(R.string.update_on);
    }

    public String getNomPreamble(int nom, String currencyName) {
        return getApplication().getResources().getString(R.string.nom_string) + " " + nom + " " + currencyName;
    }

    private void initViewModel(Interactors interactors) {
        CurrencyInRub currency = new CurrencyInRub("USD", LocalDate.of(0, 1, 1), 0, 0.0f);
        nowCurrency.set(currency);
        diffCurrency.set("0");

        alarmManager = new AlarmServiceManager(getApplication());
        updateAlarmState();

        isLoading.set(false);
        mainPartsVisibility.set(View.GONE);
        errorVisibility.set(View.GONE);

        this.interactors = interactors;
    }

    private void updateAlarmState() {
        ServiceState state = alarmManager.getServiceState();
        topLimit.set(state.topLimit);
        bottomLimit.set(state.bottomLimit);
        isServiceStarted.set(state.isStarted);
    }
}