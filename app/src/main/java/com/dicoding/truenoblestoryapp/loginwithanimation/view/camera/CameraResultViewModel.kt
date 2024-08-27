package com.dicoding.truenoblestoryapp.loginwithanimation.view.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.truenoblestoryapp.loginwithanimation.data.StoryRepository
import com.dicoding.truenoblestoryapp.loginwithanimation.data.response.UploadStoryResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class CameraResultViewModel(private val repository: StoryRepository): ViewModel() {

    private val _isResponse = MutableLiveData<UploadStoryResponse>()
    val isResponse: LiveData<UploadStoryResponse> = _isResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        _isLoading.value = false
    }

    fun uploadImage(multipartBody: MultipartBody.Part, requestBody: RequestBody) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _isResponse.value = repository.postStories(multipartBody, requestBody)
            } catch (e: HttpException) {
                _isResponse.value = Gson().fromJson(e.response()?.errorBody()?.string(), UploadStoryResponse::class.java)
            } finally {
                _isLoading.value = false
            }
        }

    }
}