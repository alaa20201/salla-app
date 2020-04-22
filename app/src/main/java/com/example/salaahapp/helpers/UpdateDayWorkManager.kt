package com.example.salaahapp.helpers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.example.salaahapp.R
import com.example.salaahapp.service.MyIntentService
import com.example.salaahapp.views.activities.fragmentsCalendar.DateActivity
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class UpdateDayWorkManager(var context: Context, workerParams: WorkerParameters) :
    Worker(context,workerParams){
//    companion object{
//        private val DEFAULT_START_TIME = "13:53" //12:01 am
//
//    }
//    private val WORK_RESULT = "work_result"
    override fun doWork(): Result {
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()
        // Set Execution Timer
        dueDate.set(Calendar.HOUR_OF_DAY, 14)
        dueDate.set(Calendar.MINUTE, 28)
        dueDate.set(Calendar.SECOND, 0)
        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }
        val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis
        val dailyWorkRequest = OneTimeWorkRequest.Builder(UpdateDayWorkManager::class.java).setInitialDelay(timeDiff, TimeUnit.MILLISECONDS).build()
        WorkManager.getInstance().enqueue(dailyWorkRequest)
        Log.d("WorkerManagerTester","Success2")

        return Result.success()
        /**
         *

        //val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        //var currenttime = dateFormat.format(Calendar.getInstance().time)
        //Log.d("WorkManagerTester",currenttime)
        //if (currenttime == DEFAULT_START_TIME){
            //      if (compareCurrentHour(13)) {
            val taskData = inputData
            val taskDataString = taskData.getString(DateActivity.MESSAGE_STATUS)
            showNotification(
                "WorkManager",
                if (taskDataString != null) taskDataString else "Message has been Sent"
            )
            //     }
        //}
        var outputData:Data = Data.Builder().putString(WORK_RESULT, "Jobs Finished").build()
        return Result.success(outputData)
        */
    }

    private fun showNotification(task: String, desc: String) {
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "task_channel"
        val channelName = "task_name"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }
        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(task)
            .setContentText(desc)
            .setSmallIcon(R.mipmap.ic_launcher)
        manager.notify(1, builder.build())
    }
    private fun compareCurrentHour(targetHour: Int): Boolean {
        val current = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return current == targetHour
    }
}