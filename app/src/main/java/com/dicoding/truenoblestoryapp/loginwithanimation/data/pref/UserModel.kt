package com.dicoding.truenoblestoryapp.loginwithanimation.data.pref

data class UserModel(
    val email: String,
    var token: String,
    val isLogin: Boolean = false
)