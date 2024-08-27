package com.dicoding.truenoblestoryapp.loginwithanimation.di

import android.content.Context
import com.dicoding.truenoblestoryapp.loginwithanimation.data.StoryRepository
import com.dicoding.truenoblestoryapp.loginwithanimation.data.pref.UserPreference
import com.dicoding.truenoblestoryapp.loginwithanimation.data.pref.dataStore
import com.dicoding.truenoblestoryapp.loginwithanimation.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return StoryRepository.getInstance(apiService, pref)
    }
}