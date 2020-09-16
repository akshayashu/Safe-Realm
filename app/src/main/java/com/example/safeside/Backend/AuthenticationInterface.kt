package com.example.safeside.Backend

import com.example.safeside.DataModel.Zones
import retrofit2.Call
import retrofit2.http.*

interface AuthenticationInterface {

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : Call<String>

    @FormUrlEncoded
    @POST("registerUser")
    fun registration(
        @Field("userName") userName: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : Call<String>

    @FormUrlEncoded
    @POST("update")
    fun update(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("newPassword") newPassword: String
    ) : Call<String>

    @GET("getAllMarkers")
    fun zones() : Call<List<Zones>>
}