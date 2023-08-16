package com.task.currencyconversion.util.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.task.currencyconversion.data.RepoOperations
import com.task.currencyconversion.util.helper.getStartOfDayInMillis
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import retrofit2.HttpException

@HiltWorker
class DeletePrevDataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val repository: RepoOperations
) : CoroutineWorker(appContext, params) {
    companion object {
        const val WORK_NAME = "deletePrevDataWorker"
    }
    override suspend fun doWork(): Result {
        return try {
            repository.deleteRatesOldData(getStartOfDayInMillis(-3))
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}