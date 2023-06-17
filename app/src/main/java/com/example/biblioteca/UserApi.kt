package com.example.biblioteca

import com.google.gson.JsonArray
import retrofit2.Call
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface UserAPI {
    @POST("postSelect/")
    @FormUrlEncoded
    fun login(@Field("query") query : String): Call<JsonObject>

    @POST("postSelect/")
    @FormUrlEncoded
    fun verificaCodice(@Field("query") query : String): Call<JsonObject>

    @POST("postInsert/")
    @FormUrlEncoded
    fun register(@Field("query") query: String): Call<JsonObject>


    @POST("postSelect/")
    @FormUrlEncoded
    fun getLibri(@Field("query") query : String): Call<JsonObject>
    @POST("postSelect/")
    @FormUrlEncoded
    fun oldPass(@Field("query") query : String): Call<JsonObject>


    @POST("postSelect/")
    @FormUrlEncoded
    fun getCronologia(@Field("query") query : String): Call<JsonObject>
    @POST("postSelect/")
    @FormUrlEncoded
    fun getPrenotazione(@Field("query") query : String): Call<JsonObject>

    @POST("postUpdate/")
    @FormUrlEncoded
    fun modificaValutazione(@Field("query") query: String): Call<JsonObject>

    @POST("postUpdate/")
    @FormUrlEncoded
    fun posticipa(@Field("query") query: String): Call<JsonObject>

    @GET
    fun getAvatar(@Url url: String) : Call<ResponseBody>


}