package com.example.freelancingapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class NotificationChannels(val context: Context) {
    val channel1 : String = "Channel1"
    val channel2 : String = "Channel2"

    fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val ch1 = NotificationChannel(
                    channel1,"Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            )
            ch1.description = "This is channel 1"

            val ch2 = NotificationChannel(
                    channel2,"Channel 2",
                    NotificationManager.IMPORTANCE_HIGH
            )
            ch2.description = "This is channel 2"
            val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(ch1)
            notificationManager.createNotificationChannel(ch2)
        }
    }

}