package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

private val NOTIFICATION_ID = 0
private val CHANNEL_ID = "download_channel"
private val CHANNEL_NAME = "Downloads"

fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context, contentIntent: Intent){
    val contentPendingIntent = PendingIntent.getActivity(applicationContext, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT)


    val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle("Download Status")
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent).addAction(R.drawable.ic_assistant_black_24dp,"View Details",contentPendingIntent)
    notify(NOTIFICATION_ID,builder.build())
}