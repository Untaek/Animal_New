package io.untaek.animal_new

import android.app.Application
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId

class App: Application(), FirebaseAuth.AuthStateListener {
    override fun onAuthStateChanged(auth: FirebaseAuth) {
        Log.d("App Token", "AuthStateChanged")
        auth.currentUser?.let { user ->
            Log.d("App Token", "User Logined")
            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
                FirebaseDatabase
                    .getInstance()
                    .getReference("message_token")
                    .child(user.uid)
                    .updateChildren(mapOf("token" to it.token))
                    .addOnSuccessListener {
                        Log.d(Messaging.TAG, "token saved")
                    }
                    .addOnFailureListener {
                        Log.d(Messaging.TAG, "token failed")
                    }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseAuth.getInstance().addAuthStateListener(this)
    }
}