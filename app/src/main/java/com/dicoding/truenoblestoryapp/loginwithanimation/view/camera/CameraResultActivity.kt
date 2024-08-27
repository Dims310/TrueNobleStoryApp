package com.dicoding.truenoblestoryapp.loginwithanimation.view.camera

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.truenoblestoryapp.loginwithanimation.R
import com.dicoding.truenoblestoryapp.loginwithanimation.data.response.UploadStoryResponse
import com.dicoding.truenoblestoryapp.loginwithanimation.databinding.ActivityCameraResultBinding
import com.dicoding.truenoblestoryapp.loginwithanimation.util.reduceFileImage
import com.dicoding.truenoblestoryapp.loginwithanimation.util.uriToFile
import com.dicoding.truenoblestoryapp.loginwithanimation.view.ViewModelFactory
import com.dicoding.truenoblestoryapp.loginwithanimation.view.main.MainActivity
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class CameraResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraResultBinding
    private var currentImageUri: Uri? = null

    private val viewModel by viewModels<CameraResultViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCameraResultBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupAction()
        showImage()

        binding.uploadButton.setOnClickListener { uploadImage() }

        viewModel.isResponse.observe(this) { r ->
            showToast(Gson().toJson(r))
        }

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun setupAction() {
        with(binding.topAppBar) {
            setNavigationIcon(R.drawable.baseline_arrow_back_ios_new_24)
            setNavigationOnClickListener { finish() }
            title = "Hasil Foto"
        }
    }

    private fun showImage() {
        currentImageUri = intent.getStringExtra(EXTRA_CAMERA_IMAGE)?.toUri()
        binding.previewImageView.setImageURI(currentImageUri)
    }

    private fun uploadImage() {
        currentImageUri?.let {
            val imageFile = uriToFile(it, this).reduceFileImage()
            Log.d("Image File", "uploadImage: ${imageFile.path}")
            val description = binding.descEditText.text.toString()

            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )

            viewModel.uploadImage(multipartBody, requestBody)
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(msg: String) {
        val msgConverted = Gson().fromJson(msg, UploadStoryResponse::class.java)

        if (msgConverted.error == true) {
            Toast.makeText(this, msgConverted.message, Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Sukses menambah cerita", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    companion object {
        const val EXTRA_CAMERA_IMAGE = "CameraX Image"
    }
}