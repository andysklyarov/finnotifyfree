package com.andysklyarov.domain

import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

val NETWORK_EXCEPTIONS = listOf<Class<*>>(
    UnknownHostException::class.java,
    SocketTimeoutException::class.java,
    ConnectException::class.java
)

fun hasNetworkException(throwable: Throwable): Boolean {
    return NETWORK_EXCEPTIONS.contains(throwable.javaClass)
}