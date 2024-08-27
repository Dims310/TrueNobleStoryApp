package com.dicoding.truenoblestoryapp.loginwithanimation.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.truenoblestoryapp.loginwithanimation.data.StoryRepository
import com.dicoding.truenoblestoryapp.loginwithanimation.data.response.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignupViewModel(private val repository: StoryRepository) : ViewModel() {
    private val _isResponse = MutableLiveData<RegisterResponse>()
    val isResponse: LiveData<RegisterResponse> = _isResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        _isLoading.value = false
    }

    fun register(name: String, email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val gson = repository.postRegister(name, email, password)
                _isResponse.value = gson
            } catch (e: HttpException) {
                _isResponse.value = Gson().fromJson(e.response()?.errorBody()?.string(), RegisterResponse::class.java)
            } finally {
                _isLoading.value = false
            }
        }
    }
}