package com.fuh.markinbook.api

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.fuh.markinbook.MainActivity
import com.fuh.markinbook.MainActivity.Companion.KEY_DISCIPLINE_TITLE
import com.fuh.markinbook.MainActivity.Companion.KEY_HOMEWORK_ID
import com.fuh.markinbook.MainActivity.Companion.KEY_LESSON_ID
import com.fuh.markinbook.MainActivity.Companion.KEY_TYPE
import com.fuh.markinbook.PreferencesManager
import com.fuh.markinbook.R
import com.fuh.markinbook.data.PushType
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        PreferencesManager.deviceToken = token
        Timber.i(PreferencesManager.deviceToken)
    }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Timber.i("Notif recieved")
        val data = remoteMessage.data
        if (data.isNotEmpty()) {
            Timber.d(data.toString())
            when (data[KEY_TYPE]) {
                PushType.HOMEWORK_MARK.title -> {
                    val args = Bundle().apply {
                        putString(KEY_TYPE,data[KEY_TYPE])
                        putString(KEY_LESSON_ID, data[KEY_LESSON_ID])
                    }
                    createNotification(
                        data[KEY_DISCIPLINE_TITLE]!!,
                        applicationContext.getString(R.string.homework_mark),
                        R.drawable.ic_mark,
                        1,
                        1,
                        args
                    )
                }
                PushType.LESSON_MARK.title -> {
                    val args = Bundle().apply {
                        putString(KEY_TYPE,data[KEY_TYPE])
                        putString(KEY_LESSON_ID, data[KEY_LESSON_ID])
                    }
                    createNotification(
                        data[KEY_DISCIPLINE_TITLE]!!,
                        applicationContext.getString(R.string.lesson_mark),
                        R.drawable.ic_mark,
                        2,
                        2,
                        args
                    )
                }
                PushType.HOMEWORK_ADDED.title -> {
                    val args = Bundle().apply {

                        putString(KEY_TYPE,data[KEY_TYPE])
                        putString(KEY_LESSON_ID, data[KEY_LESSON_ID])
                    }
                    createNotification(
                        data[KEY_DISCIPLINE_TITLE]!!,
                        applicationContext.getString(R.string.homework_added),
                        R.drawable.ic_homework_added,
                        0,
                        0,
                        args
                    )
                }
            }
        }
    }

    private fun createNotification(
        title:String,
        text: String,
        icon: Int,
        requestCode: Int,
        notificationId: Int,
        data: Bundle
    ) {
        val builder = NotificationCompat.Builder(applicationContext, "MARKINBOOK_CHANNEL_ID")
            .setContentTitle(title)
            .setContentText(text)
            .setAutoCancel(true)
            .setSmallIcon(icon)
        val mainIntent = Intent(applicationContext, MainActivity::class.java)
        mainIntent.putExtras(data)
        val activity = PendingIntent.getActivity(
            applicationContext,
            requestCode,
            mainIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        builder.setContentIntent(activity)
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        notificationManager.notify(notificationId, builder.build())
    }
}