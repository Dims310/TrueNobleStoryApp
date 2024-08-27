package com.dicoding.truenoblestoryapp.loginwithanimation.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.truenoblestoryapp.loginwithanimation.data.StoryRepository
import com.dicoding.truenoblestoryapp.loginwithanimation.data.response.StoryResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MapsViewModel(private val repository: StoryRepository) : ViewModel() {
    private val _storyResponses = MutableLiveData<StoryResponse>()
    val storyResponses: LiveData<StoryResponse> = _storyResponses

    fun getAllLocStories() {
        viewModelScope.launch {
            try {
                _storyResponses.value = repository.getAllLocStories()
            } catch (e: HttpException) {
                _storyResponses.value = Gson()
                    .fromJson(e.response()?.errorBody()?.string(), StoryResponse::class.java)
            }
        }

    }
}