package com.example.coinstest.framework;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.coinstest.interactors.Interactors;
import com.example.coinstest.presentation.MainActivityViewModel;

import java.lang.reflect.InvocationTargetException;

public final class ApplicationViewModelFactory implements ViewModelProvider.Factory {

    private static Interactors useCase;

    static void inject(Interactors newUseCase) {
        useCase = newUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (MainActivityViewModel.class.isAssignableFrom(modelClass)) {
            try {
                return modelClass.getConstructor(Interactors.class).newInstance(useCase);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        throw new IllegalStateException("ViewModel must extend MainActivityViewModel");
    }
}
