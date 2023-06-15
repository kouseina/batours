package com.example.batours.api

import com.example.batours.models.AllBookmarkResponse
import com.example.batours.models.AllCategoryResponse
import com.example.batours.models.AllDestinationResponse
import com.example.batours.models.DefaultResponse
import com.example.batours.models.DetailDestinationResponse
import com.example.batours.models.LoginResponse
import okhttp3.Credentials
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface Api {

    @FormUrlEncoded
    @POST("users/register")
    fun createUser(
        @Field("email") email:String,
        @Field("full_name") full_name:String,
        @Field("phone") phone:String,
        @Field("password") password:String,
        @Field("profile_pic_url") profile_pic_url:String,
        @Header("Authorization") authHeader: String,
    ):Call<DefaultResponse>

    @FormUrlEncoded
    @POST("users/login")
    fun userLogin(
        @Field("email") email:String,
        @Field("password") password: String,
        @Header("Authorization") authHeader: String,
    ):Call<LoginResponse>

    @GET("destinations")
    fun getAllDestination(
        @Header("Authorization") authHeader: String,
        @Query("column") columnParam: String = "rating",
        @Query("filter") filterParam: String = "desc",
        @Query("category") categoryParam: String?,
        @Query("search") searchParam: String,
    ):Call<AllDestinationResponse>

    @GET("destinations/popular")
    fun getPopularDestination(
        @Header("Authorization") authHeader: String,
    ):Call<AllDestinationResponse>

    @GET("destinations/recommendation")
    fun getRecommendationDestination(
        @Header("Authorization") authHeader: String,
    ):Call<AllDestinationResponse>

    @GET("destinations/{ID}")
    fun getDetailDestination(
        @Header("Authorization") authHeader: String,
        @Path("ID") id: Int,
    ):Call<DetailDestinationResponse>

    @GET("destinations/category")
    fun getAllCategory(
        @Header("Authorization") authHeader: String,
    ): Call<AllCategoryResponse>

    @GET("destinations/category/{CATEGORY}")
    fun getDestinationByCategory(
        @Header("Authorization") authHeader: String,
        @Path("CATEGORY") category: String,
    ):Call<AllDestinationResponse>

    @GET("bookmarks/save/{ID}")
    fun saveBookmark(
        @Header("Authorization") authHeader: String,
        @Path("ID") id: Int,
    ):Call<DefaultResponse>

    @GET("bookmarks")
    fun getAllBookmark(
        @Header("Authorization") authHeader: String,
    ):Call<AllBookmarkResponse>
}