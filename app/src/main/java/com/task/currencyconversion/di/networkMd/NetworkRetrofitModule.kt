package com.task.currencyconversion.di.networkMd

import com.task.currencyconversion.data.remote.RemoteService
import com.task.currencyconversion.util.helper.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkRetrofitModule {
    private val interceptor = OkHttpClient.Builder().addInterceptor(
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    ).connectTimeout(60, TimeUnit.SECONDS).readTimeout(
        60, TimeUnit.SECONDS
    ).build()


    private val retrofit = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create())
        .baseUrl(Constants.BASE_URL).client(interceptor).build()

    @Provides
    @Singleton
    fun remoteService(): RemoteService {
        return retrofit.create(RemoteService::class.java)
    }
}