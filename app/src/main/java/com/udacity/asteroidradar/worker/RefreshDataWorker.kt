package com.udacity.asteroidradar.worker

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.DatabaseBuilder
import com.udacity.asteroidradar.main.MainViewModel
import com.udacity.asteroidradar.repository.Repository
import retrofit2.HttpException

    class RefreshDataWorker(appContext: Context, params: WorkerParameters):
        CoroutineWorker(appContext, params) {

        companion object {
            const val WORK_NAME = "RefreshDataWorker"
        }

        @RequiresApi(Build.VERSION_CODES.N)
        override suspend fun doWork(): Result {
            val database = DatabaseBuilder.getInstance(applicationContext)
            val repository = Repository(database)
            val viewModel = MainViewModel(repository)
            return try {
                viewModel.refresh()
                Result.success()
            } catch (e: HttpException) {
                Result.retry()
            }
        }
    }
