package com.dicoding.truenoblestoryapp.loginwithanimation.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.truenoblestoryapp.loginwithanimation.data.StoryRepository
import com.dicoding.truenoblestoryapp.loginwithanimation.data.pref.UserModel
import com.dicoding.truenoblestoryapp.loginwithanimation.data.response.LoginResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val repository: StoryRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isResponse = MutableLiveData<LoginResponse>()
    val isResponse: LiveData<LoginResponse> = _isResponse

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    init {
        _isLoading.value = false
    }

    fun login(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val gson = repository.login(email, password)
                _isResponse.value = gson
            } catch (e: HttpException) {
                _isResponse.value = Gson().fromJson(e.response()?.errorBody()?.string(), LoginResponse::class.java)
            } finally {
                _isLoading.value = false
            }
        }
    }
}