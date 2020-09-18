package com.andysklyarov.finnotifyfree.di

import com.andysklyarov.finnotifyfree.alarm.AlarmReceiver
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ReceiverModule {
    @ContributesAndroidInjector
    @ReceiverScope
    abstract fun connectionReceiver() : AlarmReceiver
}