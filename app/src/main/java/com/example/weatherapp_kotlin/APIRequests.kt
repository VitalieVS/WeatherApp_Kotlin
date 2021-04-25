package com.example.weatherapp_kotlin

import com.example.weatherapp_kotlin.api.WeatherJSON
import retrofit2.Call
import retrofit2.http.GET

interface APIRequests {
    @GET("weather?q=Balti&units=metric&appid=0dbb8c4b976379671419e66c70c93ded")
    fun getWeather(): Call<WeatherJSON>
}