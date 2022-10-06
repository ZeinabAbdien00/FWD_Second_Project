package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.model.Asteroid

@Dao
interface RoomDAO {

    @Query("SELECT * FROM asteroidDatabaseTable WHERE closeApproachDate BETWEEN :afterWeekDate AND :todayDate  ORDER BY closeApproachDate DESC")
    fun getAsteroidsForWeek(todayDate : String, afterWeekDate : String): LiveData<List<Asteroid>>

    @Query("SELECT * FROM asteroidDatabaseTable WHERE closeApproachDate = :startDate ORDER BY closeApproachDate DESC")
    fun getAsteroidsForDay(startDate: String): LiveData<List<Asteroid>>

    @Query("SELECT * FROM asteroidDatabaseTable ORDER by closeApproachDate")
    fun getAll(): List<Asteroid>

    @Query("SELECT * FROM asteroidDatabaseTable ORDER BY closeApproachDate DESC")
    fun getAllAsteroids(): LiveData<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(asteroid: List<Asteroid>)

    @Query("DELETE  FROM asteroidDatabaseTable")
    fun clear()

}