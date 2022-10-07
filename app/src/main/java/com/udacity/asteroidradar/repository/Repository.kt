package com.udacity.asteroidradar.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.ApiRetrofitBuilder
import com.udacity.asteroidradar.api.asteroidList
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.constant.Constants
import com.udacity.asteroidradar.database.DatabaseBuilder
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class Repository (private val dataBase: DatabaseBuilder){

    lateinit var astroList:ArrayList<Asteroid>

    val startDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    @RequiresApi(Build.VERSION_CODES.O)
    val endDate = LocalDateTime.now().minusDays(7).toString()

    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun getAsteroidsArraylist(): ArrayList<Asteroid> {
        val jsonObject =
            JSONObject(ApiRetrofitBuilder.retrofitService.getAsteroids("", "", Constants.API_KEY))
        astroList= parseAsteroidsJsonResult(jsonObject)
        return astroList
    }

    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun getAsteroidsFromAPI() {
        withContext(Dispatchers.IO) {
            val asteroids = ApiRetrofitBuilder.ApiService.getAsteroids()
            dataBase.databaseDAO_object.insertAll(asteroids)
        }
    }

    suspend fun getAsteroidPicture(): PictureOfDay {
        return ApiRetrofitBuilder.retrofitService.getPictureOfDay(Constants.API_KEY)
    }

    val asteroidSavedList: LiveData<List<Asteroid>> =
        Transformations.map(dataBase.databaseDAO_object.getAllAsteroids()) {
            it.asteroidList()
        }

    @RequiresApi(Build.VERSION_CODES.O)
    val asteroidWeekList: LiveData<List<Asteroid>> = Transformations.map(
        dataBase.databaseDAO_object.getAsteroidsForWeek(
            startDate.toString(), endDate
        )
    ) {
        it.asteroidList()
    }

    val asteroidDayList: LiveData<List<Asteroid>> = Transformations.map(
        dataBase.databaseDAO_object.getAsteroidsForDay(
            startDate.toString()
        )
    ) {
        it.asteroidList()
    }
}