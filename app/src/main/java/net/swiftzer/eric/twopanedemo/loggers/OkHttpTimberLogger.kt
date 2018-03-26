package net.swiftzer.eric.twopanedemo.loggers

import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

/**
 * Created by eric on 26/3/2018.
 */
class OkHttpTimberLogger : HttpLoggingInterceptor.Logger {
    override fun log(message: String?) {
        Timber.tag("OkHttp").d(message)
    }
}
