package com.dicoding.truenoblestoryapp.loginwithanimation.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.truenoblestoryapp.loginwithanimation.data.StoryRepository
import com.dicoding.truenoblestoryapp.loginwithanimation.data.response.DetailStoryResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DetailViewModel(private val repository: StoryRepository): ViewModel() {
    private val _isResponse = MutableLiveData<DetailStoryResponse>()
    val isResponse: LiveData<DetailStoryResponse> = _isResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        _isLoading.value = false
    }

    fun getDetailStory(id: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _isResponse.value = repository.getDetailStories(id)
            } catch (e: HttpException) {
                _isResponse.value = Gson().fromJson(e.response()?.errorBody()?.string(), DetailStoryResponse::class.java)
            } finally {
                _isLoading.value = false
            }
        }
    }
}