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

@RequiresApi(Build.VERSION_CODES.N)
class MainViewModel(private val repository: Repository) : ViewModel() {

    private val timeFilter = MutableLiveData(AsteroidTime.ALL)

    var arraylist: ArrayList<Asteroid> = ArrayList<Asteroid>()
    val responseIncomeData: MutableLiveData<List<Asteroid>> = MutableLiveData()

    private val _picture = MutableLiveData<PictureOfDay>()
    val picture: LiveData<PictureOfDay>
        get() = _picture

    private val _navigateToDetailFragment = MutableLiveData<Asteroid?>()
    val navigateToDetailFragment
        get() = _navigateToDetailFragment

    @RequiresApi(Build.VERSION_CODES.N)
    fun refresh() {
        viewModelScope.launch {
            try {
                repository.getAsteroidsFromAPI()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    init {
        refresh()
        getAsteroidPicture()
        getAsteroids()
        _navigateToDetailFragment.value=null
    }


    @RequiresApi(Build.VERSION_CODES.O)
    val asteroids =
        Transformations.switchMap(timeFilter) {
            when (it) {
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
        TODAY,ALL
    }

}
