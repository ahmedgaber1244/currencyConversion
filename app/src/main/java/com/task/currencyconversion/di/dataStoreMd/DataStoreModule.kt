package com.task.currencyconversion.di.dataStoreMd

import android.content.Context
import com.task.currencyconversion.util.dataStore.DataStoreUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStoreUtil {
        return DataStoreUtil(context)
    }
}