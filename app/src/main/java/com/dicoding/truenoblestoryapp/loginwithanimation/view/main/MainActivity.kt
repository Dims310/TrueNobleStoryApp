package com.dicoding.truenoblestoryapp.loginwithanimation.view.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.truenoblestoryapp.loginwithanimation.R
import com.dicoding.truenoblestoryapp.loginwithanimation.data.response.ListStoryItem
import com.dicoding.truenoblestoryapp.loginwithanimation.databinding.ActivityMainBinding
import com.dicoding.truenoblestoryapp.loginwithanimation.view.ViewModelFactory
import com.dicoding.truenoblestoryapp.loginwithanimation.view.camera.CameraActivity
import com.dicoding.truenoblestoryapp.loginwithanimation.view.maps.MapsActivity
import com.dicoding.truenoblestoryapp.loginwithanimation.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityMainBinding

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (it) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.getSession().observe(this) { user ->
            Log.d("MAIN_ACTIVITY", "onCreate: ${user.token}")
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                viewModel.storyResponses.observe(this) {
                    setStoryList(it)
                }
            }
        }

        setupAction()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        })
    }

    private fun setStoryList(storyItem: PagingData<ListStoryItem>) {
        val adapter = StoryAdapter()
        adapter.submitData(lifecycle, storyItem)

        binding.rvStory.layoutManager = LinearLayoutManager(this)
        binding.rvStory.setHasFixedSize(true)
        binding.rvStory.adapter = adapter
    }

    private fun setupAction() {
        binding.topAppBar.title = "True Noble"
        binding.topAppBar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.mapsButton -> {
                    startActivity(Intent(this, MapsActivity::class.java))
                    true
                }
                R.id.logoutButton -> {
                    viewModel.logout()
                    true
                }
                else -> false
            }
        }

        if(!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.fabAdd.setOnClickListener { startCameraX() }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}