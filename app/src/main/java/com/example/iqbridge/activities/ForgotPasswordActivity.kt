package com.example.iqbridge.activities

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.iqbridge.databinding.ActivityForgotPasswordBinding
import com.example.iqbridge.utils.Base
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.btnSubmit.setOnClickListener { resetPassword() }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }

    private fun validateForm(email: String): Boolean {
        return when {
            TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.inputEmail.error = "Enter valid email address"
                false
            }
            else -> true
        }
    }

    private fun resetPassword() {
        val email = binding.etForgotPasswordEmail.text.toString()
        if (validateForm(email)) {
            val pb = Base.showProgressBar(this)
            auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                pb.cancel()
                if (task.isSuccessful) {
                    binding.inputEmail.visibility = View.GONE
                    binding.tvSubmitMsg.visibility = View.VISIBLE
                    binding.btnSubmit.visibility = View.GONE
                } else {
                    Base.showToast(this, "Cannot reset your password. Try again later.")
                }
            }
        }
    }
}