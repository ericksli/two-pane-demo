package net.swiftzer.eric.twopanedemo.loggers

import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

/**
 * Simple OkHttp logger uses [Timber] to log message.
 */
class OkHttpTimberLogger : HttpLoggingInterceptor.Logger {
    override fun log(message: String?) {
        Timber.tag("OkHttp").d(message)
    }
}
