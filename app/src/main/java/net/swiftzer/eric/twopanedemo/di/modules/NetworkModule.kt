package net.swiftzer.eric.twopanedemo.di.modules

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import net.swiftzer.eric.twopanedemo.network.DeliveryApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Eric on 3/25/2018.
 */
@Module
class NetworkModule(private val apiEndpoint: String) {
    @Provides
    fun provideOkHttpClient() = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, moshiConverterFactory: MoshiConverterFactory, rxJava2CallAdapterFactory: RxJava2CallAdapterFactory) =
            Retrofit.Builder()
                    .baseUrl(apiEndpoint)
                    .client(okHttpClient)
                    .addConverterFactory(moshiConverterFactory)
                    .addCallAdapterFactory(rxJava2CallAdapterFactory)
                    .build()

    @Provides
    fun provideApiClient(retrofit: Retrofit) = retrofit.create(DeliveryApi::class.java)

    @Provides
    fun provideMoshi() = Moshi.Builder().build()

    @Provides
    fun provideMoshiConverterFactory(moshi: Moshi) = MoshiConverterFactory.create(moshi)

    @Provides
    fun provideRxJava2CallAdapterFactory() = RxJava2CallAdapterFactory.create()
}