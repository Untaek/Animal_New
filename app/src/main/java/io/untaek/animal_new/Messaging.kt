package io.untaek.animal_new

import android.annotation.TargetApi
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.untaek.animal_new.activity.MainActivity
import io.untaek.animal_new.activity.postdetail.PostDetailActivity

class Messaging: FirebaseMessagingService() {

    companion object {
        const val TAG = "Messaging"
    }

    lateinit var manager: NotificationManager

    override fun onNewToken(token: String?) {
        Log.d(TAG, "Refresh token: $token")

        val uid = FirebaseAuth
            .getInstance()
            .uid ?: "0"

        FirebaseDatabase
            .getInstance()
            .getReference("message_token")
            .child(uid)
            .updateChildren(mapOf("token" to token))
            .addOnSuccessListener {
                Log.d(TAG, "token saved")
            }
    }

    override fun onMessageReceived(message: RemoteMessage?) {
        Log.d(TAG, "onMessageReceived ${message.toString()}")
        Log.d(TAG, "onMessageReceived from ${message?.from}")
        Log.d(TAG, "onMessageReceived data ${message?.data}")
        Log.d(TAG, "onMessageReceived notification data ${message?.notification?.body}")

        manager = getSystemService(Activity.NOTIFICATION_SERVICE) as NotificationManager

        message?.notification?.let {
            createChannel("comment", NotificationManagerCompat.IMPORTANCE_DEFAULT)

            val notification = NotificationCompat.Builder(this, "comment")
                .setContentText(it.body)
                .setContentTitle(it.title)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true)

            val intent = Intent(this, PostDetailActivity::class.java).apply {
                putExtras(message.toIntent().extras!!)
                putExtra(PostDetailActivity.FROM, PostDetailActivity.FROM_NOTIFICATION)
            }

            Log.d("Messaging", "${intent.extras}")

            val stackBuilder = TaskStackBuilder.create(this)
                .addParentStack(MainActivity::class.java)
                .addParentStack(PostDetailActivity::class.java)
                .addNextIntent(intent)

            val pending = stackBuilder.getPendingIntent(
                100,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            notification.setContentIntent(pending)
            manager.notify(15, notification.build())
        }
    }

    @TargetApi(26)
    private fun createChannel(id: String, importance: Int) {

        val channel = NotificationChannel(id, this.getString(R.string.app_name), importance)
        channel.description = "notification"
        manager.createNotificationChannel(channel)
    }

    override fun onDeletedMessages() {
        Log.d(TAG, "onDeletedMessages")
    }
}