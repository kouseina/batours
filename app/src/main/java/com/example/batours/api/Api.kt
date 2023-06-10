package com.example.batours.api

import com.example.batours.models.DefaultResponse
import com.example.batours.models.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Api {

    @FormUrlEncoded
    @POST("users/register")
    fun createUser(
        @Field("email") email:String,
        @Field("full_name") full_name:String,
        @Field("phone") phone:String,
        @Field("password") password:String,
        @Field("profile_pic_url") profile_pic_url:String
    ):Call<DefaultResponse>

    @FormUrlEncoded
    @POST("users/login")
    fun userLogin(
        @Field("email") email:String,
        @Field("password") password: String
    ):Call<LoginResponse>
}