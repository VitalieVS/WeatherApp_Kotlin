package com.example.weatherapp_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

class MainActivity : AppCompatActivity() {
    private val API_KEY: String = "0dbb8c4b976379671419e66c70c93ded"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide(); // hide actionBar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        ) // hide status bar

        setContentView(R.layout.activity_main)

        getCurrentData()

        refreshCardView.setOnClickListener {
            getCurrentData()
        }

    }

    private fun getCurrentData() {
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIRequests::class.java)

        GlobalScope.launch(Dispatchers.IO) { //Dispatchers puts in a thread that manages data
            try {
                val response = api.getWeather().awaitResponse()
                if (response.isSuccessful) {
                    val data = response.body()!!

                    withContext(Dispatchers.Main) {
                        cityView.text = data.name
                        windSpeedView.text = data.wind.speed.toString()
                        minTemperatureView.text = getString(R.string.tempValue, data.main.tempMin)
                        weatherValueView.text = getString(R.string.tempValue, data.main.temp)
                        maxTemperatureView.text = getString(R.string.tempValue,  data.main.tempMax)
                        weatherStatus.text = data.weather[0].main
                        updatedView.text = Calendar.getInstance().get(Calendar.HOUR_OF_DAY).toString()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        applicationContext, "Something wrong", Toast.LENGTH_SHORT).show()

                    mainWindow.isVisible = false
                    errorText.isVisible = true
            }

        }
    }
}


}