package com.dicoding.scancare.ui.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.dicoding.scancare.R
import com.dicoding.scancare.ViewModelFactory
import com.dicoding.scancare.ui.main.MainActivity
import com.dicoding.scancare.ui.register.UserViewModel
import com.dicoding.scancare.ui.welcome.WelcomeActivity

class SplashScreenActivity : AppCompatActivity() {

    private val userViewModel by viewModels<UserViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        userViewModel.getSession().observe(this) { user ->
            if (user.isLogin) {
                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
            } else {
                startActivity(Intent(this@SplashScreenActivity, WelcomeActivity::class.java))
            }
            finish()
        }

        val splash = Thread {
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        splash.start()
    }
}
