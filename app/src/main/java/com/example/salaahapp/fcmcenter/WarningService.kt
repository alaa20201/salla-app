package com.example.salaahapp.fcmcenter

import android.R
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.salaahapp.ui.warning.WarningActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class WarningService: FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification!!.body != null) {
            Log.e(
                "FIREBASE",
                "Message Notification Body: " + remoteMessage.notification!!.body
            )
            sendNotification(remoteMessage)
        }
    }

    private fun sendNotification(remoteMessage: RemoteMessage) {
        val notification = remoteMessage.notification
        val intent = Intent(this, WarningActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder =
            NotificationCompat.Builder(this, "101")
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_dialog_alert))
                .setSmallIcon(R.drawable.ic_dialog_alert)
                .setContentTitle(notification!!.title)
                .setContentText(notification.body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }

}