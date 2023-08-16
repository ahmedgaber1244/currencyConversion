package com.task.currencyconversion.util.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.task.currencyconversion.data.RepoOperations
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import retrofit2.HttpException

@HiltWorker
class DownloadDataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val repository: RepoOperations
) : CoroutineWorker(appContext, params) {
    companion object {
        const val WORK_NAME = "downloadDataWorker"
    }

    override suspend fun doWork(): Result {
        return try {
            repository.ratesUpToDate()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

}