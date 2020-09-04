package com.andysklyarov.finnotify.framework.receivers

data class ServiceState(
    val isStarted: Boolean,
    val timeToStartInMillis: Long,
    val topLimit: Float,
    val bottomLimit: Float
) {
    constructor(isStarted: Boolean, prevData: ServiceState) : this(
        isStarted,
        prevData.timeToStartInMillis,
        prevData.topLimit,
        prevData.bottomLimit
    )
}