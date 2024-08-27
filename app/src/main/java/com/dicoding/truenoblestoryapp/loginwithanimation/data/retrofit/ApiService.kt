package com.dicoding.truenoblestoryapp.loginwithanimation.data.retrofit

import com.dicoding.truenoblestoryapp.loginwithanimation.data.response.DetailStoryResponse
import com.dicoding.truenoblestoryapp.loginwithanimation.data.response.LoginResponse
import com.dicoding.truenoblestoryapp.loginwithanimation.data.response.RegisterResponse
import com.dicoding.truenoblestoryapp.loginwithanimation.data.response.StoryResponse
import com.dicoding.truenoblestoryapp.loginwithanimation.data.response.UploadStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("location") location: Int?
    ): StoryResponse

    @GET("stories/{id}")
    suspend fun getDetailStories(
        @Path("id") id: String
    ): DetailStoryResponse

    @Multipart
    @POST("stories")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): UploadStoryResponse


}