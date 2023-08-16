package com.task.currencyconversion.di.localDbMd

import android.content.Context
import androidx.room.Room
import com.task.currencyconversion.data.local.CurrenciesDao
import com.task.currencyconversion.data.local.CurrenciesDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDbModule {
    @Provides
    @Singleton
    fun dbInst(@ApplicationContext context: Context?): CurrenciesDb {
        return Room.databaseBuilder(context!!, CurrenciesDb::class.java, "CurrentsDB")
            .allowMainThreadQueries().fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun dbOperationsInst(db: CurrenciesDb): CurrenciesDao {
        return db.dbOperations()
    }
}