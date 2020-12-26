package com.fuh.markinbook

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.fuh.markinbook.data.database.DatabaseBuilder
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import timber.log.Timber
import timber.log.Timber.DebugTree


class MarkInBookApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        PreferencesManager.init(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
        FirebaseApp.initializeApp(this)
        DatabaseBuilder.initialize(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel("MARKINBOOK_CHANNEL_ID", this.getString(R.string.notification), NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = this.getString(R.string.notification_description)
            channel.setShowBadge(true)

            val notificationManager = this.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}