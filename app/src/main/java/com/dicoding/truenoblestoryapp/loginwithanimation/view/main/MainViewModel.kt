package com.dicoding.truenoblestoryapp.loginwithanimation.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.truenoblestoryapp.loginwithanimation.data.StoryRepository
import com.dicoding.truenoblestoryapp.loginwithanimation.data.pref.UserModel
import com.dicoding.truenoblestoryapp.loginwithanimation.data.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    val storyResponses: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStories().cachedIn(viewModelScope)

    fun getSession(): LiveData<UserModel> {
        return storyRepository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            storyRepository.logout()
        }
    }
}