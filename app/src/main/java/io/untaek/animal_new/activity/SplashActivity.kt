package io.untaek.animal_new.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = if (FirebaseAuth.getInstance().currentUser != null){
            Intent(this, MainActivity::class.java)
        }else {
            Intent(this, LoginActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}