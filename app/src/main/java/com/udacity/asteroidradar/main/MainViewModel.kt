package com.udacity.asteroidradar.main

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.DatabaseBuilder
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import com.udacity.asteroidradar.recycler_view.Adapter
import com.udacity.asteroidradar.repository.Repository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {

    private val timeFilter = MutableLiveData(AsteroidTime.ALL)

    var arraylist: ArrayList<Asteroid> = ArrayList<Asteroid>()
    val responseIncomeData: MutableLiveData<List<Asteroid>> = MutableLiveData()

    private val _picture = MutableLiveData<PictureOfDay>()
    val picture: LiveData<PictureOfDay>
        get() = _picture

    @RequiresApi(Build.VERSION_CODES.O)
    val asteroids =
        Transformations.switchMap(timeFilter) {
            when (it) {
                AsteroidTime.WEEK -> repository.asteroidWeekList
                AsteroidTime.TODAY -> repository.asteroidDayList
                else -> repository.asteroidSavedList
            }
        }
    @RequiresApi(Build.VERSION_CODES.N)
    fun getAsteroids() {
        viewModelScope.launch {
            try {
                responseIncomeData.value = repository.getAsteroidsArraylist()
            } catch (e: Exception) {
                e.printStackTrace()
                getAsteroids()
            }
        }
    }
    fun getAsteroidPicture() {
        viewModelScope.launch {
            try {
                _picture.value = repository.getAsteroidPicture()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun onChangeFilter(_filter: AsteroidTime) {
        timeFilter.postValue(_filter)
    }

    enum class AsteroidTime {
        TODAY,WEEK,ALL
    }

    class AsteroidClickListener(val clickListener: (Asteroid) -> Unit) {
        fun onClickAsteroid(data: Asteroid) = clickListener(data)
    }

}
