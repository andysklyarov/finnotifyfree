package com.andysklyarov.finnotify.framework.receivers;

import androidx.annotation.NonNull;

public final class ServiceState {
    public final boolean isStarted;
    public final float topLimit;
    public final float bottomLimit;
    public final long timeToStartInMillis;

    ServiceState(boolean isStarted, long timeToStartInMillis, float topLimit, float bottomLimit) {
        this.isStarted = isStarted;
        this.topLimit = topLimit;
        this.bottomLimit = bottomLimit;
        this.timeToStartInMillis = timeToStartInMillis;
    }

    ServiceState(boolean isStarted, @NonNull ServiceState prevData) {
        this.isStarted = isStarted;
        this.topLimit = prevData.topLimit;
        this.bottomLimit = prevData.bottomLimit;
        this.timeToStartInMillis = prevData.timeToStartInMillis;
    }
}