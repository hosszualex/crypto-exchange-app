package com.example.cryptoexchange.domain

import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ErrorUtil {
    const val ERROR_NO_INTERNET = 600
    val ERROR_NO_INTERNET_EXCEPTION =
        Throwable("No internet connection.", Throwable(ERROR_NO_INTERNET.toString()))

    fun parsedErrorThrowable(exception: Throwable): Throwable {
        return when (exception) {
            is UnknownHostException,
            is SocketTimeoutException,
            is ConnectException,
            is SocketException,
            ->
                ERROR_NO_INTERNET_EXCEPTION
            else -> exception
        }
    }
}
