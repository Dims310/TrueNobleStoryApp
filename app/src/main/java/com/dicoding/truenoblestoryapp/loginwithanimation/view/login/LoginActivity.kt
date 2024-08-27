package com.dicoding.truenoblestoryapp.loginwithanimation.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.truenoblestoryapp.loginwithanimation.data.pref.UserModel
import com.dicoding.truenoblestoryapp.loginwithanimation.data.response.LoginResponse
import com.dicoding.truenoblestoryapp.loginwithanimation.databinding.ActivityLoginBinding
import com.dicoding.truenoblestoryapp.loginwithanimation.view.ViewModelFactory
import com.dicoding.truenoblestoryapp.loginwithanimation.view.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityLoginBinding
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.activityLogin) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupAction()
        playAnimation()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val name = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val email = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEdit = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val password = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEdit = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val loginButton = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title, name, email, emailEdit, password, passwordEdit, loginButton
            )
            startDelay = 100
            start()
        }
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            email = binding.emailEditText.text.toString()
            password = binding.passwordEditText.text.toString()

            viewModel.login(email, password)
        }

        viewModel.isResponse.observe(this) {
            showAlertDialog(it)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showAlertDialog(r: LoginResponse) {
        val alertDialog = AlertDialog.Builder(this)

        if (r.error == true) {
            alertDialog.apply {
                setTitle("Gagal!")
                setMessage(r.message)
                setNegativeButton("Kembali", null)
                create()
                show()
            }
        } else {
            val loginResult = r.loginResult
            val user = UserModel(
                email,
                loginResult?.token.toString(),
                true
            )

            viewModel.saveSession(user)

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    private fun showLoading(b: Boolean) {
        if (b) {
            binding.progressBar.show()
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}