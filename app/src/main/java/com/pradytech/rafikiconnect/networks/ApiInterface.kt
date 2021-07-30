package com.pradytech.rafikiconnect.networks

import com.pradytech.rafikiconnect.models.Resultstuff
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {
    companion object{
        const val BASE_URL="https://rafiki.axecredits.com/android/"
    }

    @FormUrlEncoded
    @POST("login.php")
    suspend fun Login(
        @Field("email") email: String,
        @Field("pass") pass: String,
        @Field("type") type: String


    ): Resultstuff


    @FormUrlEncoded
    @POST("registration.php")
    suspend fun borrowerRegstration(
        @Field("email") email: String,
        @Field("phone") phone: Int,
        @Field("idno") idno: Int,
        @Field("occupation") occupation: String,
        @Field("county")county: String,
        @Field("subcounty") subcounty: String,
        @Field("type") type: String
    ): Resultstuff


    @FormUrlEncoded
    @POST("registration.php")
    suspend fun lenderRegstration(
        @Field("email") email: String,
        @Field("phone") phone: Int,
        @Field("idno") idno: Int,
        @Field("category") category: String,
        @Field("county")county: String,
        @Field("subcounty") subcounty: String,
        @Field("type") type: String
    ): Resultstuff

    @FormUrlEncoded
    @POST("forgotpassword.php")
    suspend fun fgtpass(
        @Field("email") email: String,
        @Field("type") type: String
    ): Resultstuff

}