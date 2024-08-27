package com.dicoding.truenoblestoryapp.loginwithanimation.view.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.dicoding.truenoblestoryapp.loginwithanimation.R
import com.dicoding.truenoblestoryapp.loginwithanimation.data.response.DetailStoryResponse
import com.dicoding.truenoblestoryapp.loginwithanimation.databinding.ActivityDetailBinding
import com.dicoding.truenoblestoryapp.loginwithanimation.util.dateFormatter
import com.dicoding.truenoblestoryapp.loginwithanimation.view.ViewModelFactory

class DetailActivity : AppCompatActivity() {
    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var storyId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupAction()

        storyId = intent.getStringExtra(STORY_ID)!!

        viewModel.getDetailStory(storyId)

        viewModel.isResponse.observe(this) {
            setupDetail(it)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setupAction() {
        with(binding.topAppBar) {
            navigationIcon =
                AppCompatResources.getDrawable(this@DetailActivity, R.drawable.baseline_arrow_back_ios_new_24)
            setNavigationOnClickListener { finish() }
        }
    }

    private fun setupDetail(r: DetailStoryResponse) {
        if (r.error == true) {
            Toast.makeText(this, r.message, Toast.LENGTH_SHORT).show()
            finish()
        } else {
            val detailStoryResult = r.story

            binding.topAppBar.title = detailStoryResult?.name

            Glide.with(this)
                .load(detailStoryResult?.photoUrl)
                .into(binding.tvItemPhoto)

            binding.tvDate.text = dateFormatter(detailStoryResult?.createdAt!!)
            binding.tvItemDesc.text = detailStoryResult?.description
        }
    }

    private fun showLoading(b: Boolean) {
        if (b) {
            binding.progressBar.show()
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val STORY_ID = "story_id"
    }
}