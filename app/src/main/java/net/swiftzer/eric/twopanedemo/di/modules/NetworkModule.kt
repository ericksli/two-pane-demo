package net.swiftzer.eric.twopanedemo.di.modules

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import net.swiftzer.eric.twopanedemo.loggers.OkHttpTimberLogger
import net.swiftzer.eric.twopanedemo.network.DeliveryApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Network-related module.
 */
@Module
class NetworkModule(private val apiEndpoint: String) {
    @Provides
    fun provideOkHttpClient(logger: Interceptor): OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(logger)
            .build()

    @Provides
    fun provideOkHttpLogger(): Interceptor = HttpLoggingInterceptor(OkHttpTimberLogger()).apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, moshiConverterFactory: MoshiConverterFactory, rxJava2CallAdapterFactory: RxJava2CallAdapterFactory): Retrofit =
            Retrofit.Builder()
                    .baseUrl(apiEndpoint)
                    .client(okHttpClient)
                    .addConverterFactory(moshiConverterFactory)
                    .addCallAdapterFactory(rxJava2CallAdapterFactory)
                    .build()

    @Provides
    fun provideApiClient(retrofit: Retrofit): DeliveryApi = retrofit.create(DeliveryApi::class.java)

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    fun provideMoshiConverterFactory(moshi: Moshi): MoshiConverterFactory = MoshiConverterFactory.create(moshi)

    @Provides
    fun provideRxJava2CallAdapterFactory(): RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()
}
