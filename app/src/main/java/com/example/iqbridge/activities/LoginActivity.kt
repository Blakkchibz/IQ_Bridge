package com.example.iqbridge.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.iqbridge.R
import com.example.iqbridge.databinding.ActivityLoginBinding
import com.example.iqbridge.firebase.FireBaseClass
import com.example.iqbridge.models.UserModel
import com.example.iqbridge.utils.Base
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth:FirebaseAuth

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }
        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        binding.btnSignIn.setOnClickListener {
            signInUser()
        }

        binding.btnSignInWithGoogle.setOnClickListener { signInWithGoogle() }

    }

    private fun signInUser()
    {
        val email = binding.etSinInEmail.text.toString()
        val password = binding.etSinInPassword.text.toString()
        if (validateForm(email, password))
        {
            val pb = Base.showProgressBar(this)
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task->
                    if (task.isSuccessful)
                    {
                        pb.cancel()
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
                    }
                    else
                    {
                        Base.showToast(this,"Can't login currently. Try after sometime")
                        pb.cancel()
                    }
                }
        }
    }

    private fun signInWithGoogle()
    {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {result->
        if (result.resultCode == RESULT_OK)
        {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful)
        {
            val account:GoogleSignInAccount? = task.result
            if (account!=null){
                updateUI(account)
            }
        }
        else
        {
            Base.showToast(this,"SignIn Failed, Try again later")
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val pb = Base.showProgressBar(this)
        val credential = GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful)
            {
                val name = account.displayName!!
                val id = auth.uid!!
                val email = account.email!!
                FireBaseClass().doesDocumentExist(FireBaseClass().getCurrentUserId())
                    .addOnSuccessListener { exist->
                        if (!exist){
                            val userInfo = UserModel(name = name, id = id, emailId = email)
                            FireBaseClass().registerUser(userInfo)
                        }
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
                        pb.cancel()
                    }
            }
            else
            {
                Base.showToast(this,"Can't login currently. Try after sometime")
                pb.cancel()
            }
        }

    }

    private fun validateForm(email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches()->{
                binding.tilEmail.error = "Enter valid email address"
                false
            }
            TextUtils.isEmpty(password)->{
                binding.tilPassword.error = "Enter password"
                false
            }
            else -> { true }
        }
    }
}