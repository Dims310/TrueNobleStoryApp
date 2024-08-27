package com.dicoding.truenoblestoryapp.loginwithanimation.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.truenoblestoryapp.loginwithanimation.data.pref.UserModel
import com.dicoding.truenoblestoryapp.loginwithanimation.data.pref.UserPreference
import com.dicoding.truenoblestoryapp.loginwithanimation.data.response.DetailStoryResponse
import com.dicoding.truenoblestoryapp.loginwithanimation.data.response.ListStoryItem
import com.dicoding.truenoblestoryapp.loginwithanimation.data.response.LoginResponse
import com.dicoding.truenoblestoryapp.loginwithanimation.data.response.RegisterResponse
import com.dicoding.truenoblestoryapp.loginwithanimation.data.response.StoryResponse
import com.dicoding.truenoblestoryapp.loginwithanimation.data.response.UploadStoryResponse
import com.dicoding.truenoblestoryapp.loginwithanimation.data.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }

    suspend fun postRegister(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }

    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
    }

    suspend fun getDetailStories(id: String): DetailStoryResponse {
        return apiService.getDetailStories(id)
    }

    suspend fun postStories(multipartBody: MultipartBody.Part, requestBody: RequestBody): UploadStoryResponse {
        return apiService.uploadImage(multipartBody, requestBody)
    }

    suspend fun getAllLocStories(): StoryResponse {
        return apiService.getStories(null, null, 1)
    }

    companion object {
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ) = StoryRepository(userPreference, apiService)
    }
}