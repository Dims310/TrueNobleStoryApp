package com.dicoding.truenoblestoryapp.loginwithanimation.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.truenoblestoryapp.loginwithanimation.data.StoryRepository
import com.dicoding.truenoblestoryapp.loginwithanimation.di.Injection
import com.dicoding.truenoblestoryapp.loginwithanimation.view.camera.CameraResultViewModel
import com.dicoding.truenoblestoryapp.loginwithanimation.view.detail.DetailViewModel
import com.dicoding.truenoblestoryapp.loginwithanimation.view.login.LoginViewModel
import com.dicoding.truenoblestoryapp.loginwithanimation.view.main.MainViewModel
import com.dicoding.truenoblestoryapp.loginwithanimation.view.maps.MapsViewModel
import com.dicoding.truenoblestoryapp.loginwithanimation.view.signup.SignupViewModel

class ViewModelFactory(private val repository: StoryRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
            }
            modelClass.isAssignableFrom(CameraResultViewModel::class.java) -> {
                CameraResultViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        fun getInstance(context: Context) = ViewModelFactory(Injection.provideRepository(context))
    }
}