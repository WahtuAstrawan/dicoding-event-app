package com.example.dicodingevent.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.dicodingevent.R
import com.example.dicodingevent.data.remote.api.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class DailyRemainderWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    private var resultStatus: Result? = null

    override fun doWork(): Result {
        return runBlocking {
            getUpcomingEvent()
        }
    }

    private suspend fun getUpcomingEvent(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val response = ApiConfig.getApiService().getEventsWithLimit(STATUS, LIMIT)
                val event = response.listEvents[0]
                showNotification(event.name, "Acara dimulai pada ${event.beginTime}")
                resultStatus = Result.success()
            } catch (e: Exception) {
                showNotification(
                    "Get Upcoming Event Failed",
                    "Terjadi kesalahan memuat data, coba lagi"
                )
                resultStatus = Result.failure()
            }
            resultStatus as Result
        }
    }

    private fun showNotification(title: String, description: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notification.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }

    companion object {
        private const val STATUS = "-1"
        private const val LIMIT = "1"
        const val CHANNEL_ID = "channel_daily_remainder"
        const val CHANNEL_NAME = "dicoding_channel"
        const val NOTIFICATION_ID = 1
    }
}