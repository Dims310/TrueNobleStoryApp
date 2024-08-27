package com.dicoding.truenoblestoryapp.loginwithanimation.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.truenoblestoryapp.loginwithanimation.R
import com.dicoding.truenoblestoryapp.loginwithanimation.data.response.RegisterResponse
import com.dicoding.truenoblestoryapp.loginwithanimation.databinding.ActivitySignupBinding
import com.dicoding.truenoblestoryapp.loginwithanimation.view.ViewModelFactory
import com.dicoding.truenoblestoryapp.loginwithanimation.view.login.LoginActivity

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    private val viewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var customButton: MyButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.activitySignup) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        customButton = binding.customButton

        setupAction()
        playAnimation()
        setCustomButtonEnable(false)
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val name = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEdit = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val email = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEdit = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val password = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEdit = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signupButton = ObjectAnimator.ofFloat(binding.customButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title, name, nameEdit, email, emailEdit, password, passwordEdit, signupButton
            )
            startDelay = 100
            start()
        }

    }

    private fun setupAction() {

        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val b: Boolean
                if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    binding.emailEditTextLayout.error = "Email tidak valid"
                    b = false
                } else {
                    binding.emailEditTextLayout.isErrorEnabled = false
                    b = true
                }
                setCustomButtonEnable(b)
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        binding.nameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val b: Boolean
                if (binding.nameEditText.text?.isEmpty() == true) {
                    binding.nameEditTextLayout.error = "Nama masih kosong"
                    b = false
                } else {
                    binding.nameEditTextLayout.isErrorEnabled = false
                    b = true
                }
                setCustomButtonEnable(b)
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        binding.customButton.setOnClickListener {
            name = binding.nameEditText.text.toString()
            email = binding.emailEditText.text.toString()
            password = binding.passwordEditText.text.toString()

            if (name.isNotEmpty()) {
                if (!binding.emailEditTextLayout.isErrorEnabled && !binding.passwordEditTextLayout.isErrorEnabled) {
                    viewModel.register(name, email, password)
                }
            } else {
                binding.nameEditTextLayout.error = getString(R.string.empty_name)
            }
        }

        viewModel.isResponse.observe(this) {
            showAlertDialog(it)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setCustomButtonEnable(b: Boolean) {
        if (b) {
            customButton.isEnabled = true
        } else {
            customButton.isEnabled = false
        }
    }

    private fun showAlertDialog(r: RegisterResponse) {
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
            alertDialog.apply {
                setTitle("Berhasil!")
                setMessage("Akun berhasil dibuat. Yuk, login dan ceritakan momen menarikmu!")
                setPositiveButton("Login") { _, _ ->
                    val intent = Intent(context, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
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