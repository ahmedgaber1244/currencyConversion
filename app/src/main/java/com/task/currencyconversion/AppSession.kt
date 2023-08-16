package com.task.currencyconversion

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.task.currencyconversion.util.work.DeletePrevDataWorker
import com.task.currencyconversion.util.work.DownloadCurrenciesWorker
import com.task.currencyconversion.util.work.DownloadDataWorker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class AppSession : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    private val applicationScope = CoroutineScope(Dispatchers.Default)
    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            downloadCurrenciesWorker()
            downloadDataWorker()
            deletePrevDataWorker()
        }
    }

    private fun downloadCurrenciesWorker() {
        val workRequest = OneTimeWorkRequestBuilder<DownloadCurrenciesWorker>().setConstraints(
                Constraints.Builder().build()
            ).build()
        WorkManager.getInstance(this).enqueue(workRequest)
    }

    private fun deletePrevDataWorker() {
        val repeatingRequest =
            PeriodicWorkRequestBuilder<DeletePrevDataWorker>(3, TimeUnit.DAYS).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            DeletePrevDataWorker.WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, repeatingRequest
        )
    }

    private fun downloadDataWorker() {
        val constraints = Constraints.Builder().setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.UNMETERED).build()
        val repeatingRequest =
            PeriodicWorkRequestBuilder<DownloadDataWorker>(1, TimeUnit.DAYS).setConstraints(
                constraints
            ).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            DownloadDataWorker.WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, repeatingRequest
        )
    }
    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder().setWorkerFactory(workerFactory).build()
    }

}