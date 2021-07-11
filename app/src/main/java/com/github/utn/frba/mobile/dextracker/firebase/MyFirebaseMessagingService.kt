package com.github.utn.frba.mobile.dextracker.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.github.utn.frba.mobile.dextracker.LoginActivity
import com.github.utn.frba.mobile.dextracker.MyPreferences
import com.github.utn.frba.mobile.dextracker.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.data.takeUnless { it.isEmpty() }
            ?.let {
                val notification = when (it["type"]) {
                    POKEDEX -> {
                        val frag = RedirectToPokedex(
                            userId = it["userId"]!!,
                            dexId = it["dexId"]!!,
                        )
                        val title = getString(R.string.dex_update_notification_title)
                        val body = getString(
                            R.string.dex_update_notification_body,
                            it["username"],
                            it["game"],
                        )

                        Triple(frag, title, body)
                    }
                    else -> null
                }

                notification?.let { (redirection, title, body) ->
                    redirect = redirection
                    showNotification(title = title, body = body)
                }
            }
    }

    override fun onNewToken(newToken: String) {
        MyPreferences.setFirebaseToken(this, newToken)

        super.onNewToken(newToken)
    }

    private fun showNotification(title: String, body: String) {
        val intent = Intent(this, LoginActivity::class.java)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val requestCode = 0

        val pendingIntent = PendingIntent.getActivity(
            this, requestCode, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationId = 0
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "FIREBASE"
        private const val CHANNEL_ID = "CHANNEL_1"
        private const val POKEDEX = "POKEDEX"
    }
}