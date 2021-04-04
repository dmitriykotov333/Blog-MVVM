package com.kotdev.blog.app.api.auth

import com.kotdev.blog.app.api.auth.response.LoginResponse
import com.kotdev.blog.app.api.auth.response.RegistrationResponse
import com.kotdev.blog.di.auth.AuthScope
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

@AuthScope
interface AuthService {

    @POST("account/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") email: String,
        @Field("password") password: String,
    ): LoginResponse

    @POST("account/register")
    @FormUrlEncoded
    suspend fun register(
        @Field("email") email: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("password2") password2: String,
    ): RegistrationResponse
}