package com.example.coinstest.framework;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.coinstest.interactors.Interactors;
import com.example.coinstest.presentation.MainActivityViewModel;

import java.lang.reflect.InvocationTargetException;

    public final class ApplicationViewModelFactory extends ViewModelProvider.NewInstanceFactory   {

    @NonNull
    private final Application application;
    private static Interactors useCase;

    public ApplicationViewModelFactory(@NonNull Application application) {
        this.application = application;
    }

    static void inject(Interactors newUseCase) {
        useCase = newUseCase;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if (modelClass == MainActivityViewModel.class) {
            try {
                return modelClass.getConstructor(Application.class, Interactors.class).newInstance(application, useCase);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        throw new IllegalStateException("ViewModel must extend MainActivityViewModel");
    }
}
