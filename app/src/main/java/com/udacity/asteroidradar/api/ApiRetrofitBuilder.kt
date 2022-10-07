package com.udacity.asteroidradar.api

import android.os.Build
import androidx.annotation.RequiresApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.constant.Constants
import com.udacity.asteroidradar.constant.Constants.BASE_URL
import com.udacity.asteroidradar.model.Asteroid
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object ApiRetrofitBuilder {


    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .build()
    }

    val retrofitService : ApiRequest by lazy{
        retrofit.create(ApiRequest::class.java)
    }

    object ApiService {
        val retrofitService: ApiRequest by lazy {
            retrofit.create(ApiRequest::class.java)
        }

        @RequiresApi(Build.VERSION_CODES.N)
        suspend fun getAsteroids(): List<Asteroid> {
            val response = retrofitService.getAsteroids("", "", Constants.API_KEY)
            val jsonObj = JSONObject(response)
            return parseAsteroidsJsonResult(jsonObj)

        }

        suspend fun getAstPic() = retrofitService.getPictureOfDay(Constants.API_KEY)
    }
}