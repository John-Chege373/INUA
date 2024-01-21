package com.example.inua.ui

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.inua.MainActivity
import com.example.inua.R
import com.example.inua.auth.SignIn

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        //HIDE ACTION BAR
        supportActionBar?.hide()
        val windowInsetsController = WindowCompat.getInsetsController(
            window,
            window.decorView
        )
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat
            .BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        //ANIMATE THE LOGO
        animateLogo()

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, SignIn::class.java))
            finish()
        }, 3000)
    }

    private fun animateLogo() {
        val imageView = findViewById<ImageView>(R.id.logo)
        ObjectAnimator.ofFloat(imageView, "scaleX", 0.5f, 1f)
            .apply {
                duration = 1000
                interpolator = AccelerateDecelerateInterpolator()
            }.start()
        ObjectAnimator.ofFloat(imageView, "scaleY", 0.6f, 1f)
            .apply {
                duration = 1000
                interpolator = AccelerateDecelerateInterpolator()
            }.start()
    }
}