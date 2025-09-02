package com.example.iqbridge.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.iqbridge.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var splashImage: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        splashImage = binding.splashImage

        animatedZoomOut()

    }

    private fun animatedZoomOut(){
        splashImage.animate()
            .scaleX(0.4f)
            .scaleY(0.4f)
            .setDuration(1300)
            .withEndAction {
                animatedZoomIn()
            }
            .start()
    }

    private fun animatedZoomIn(){
        splashImage.animate()
            .scaleX(500f)
            .scaleY(500f)
            .setDuration(800)
            .withEndAction {
                startNewActivity()
            }
            .start()
    }

    private fun startNewActivity(){
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }


}