package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.model.Asteroid


@Database(entities = [Asteroid::class],version = 1 , exportSchema = false)
abstract class DatabaseBuilder : RoomDatabase() {

    abstract val databaseDAO_object :RoomDAO

    companion object {
        @Volatile
        private lateinit var instance: DatabaseBuilder

        fun getInstance(context: Context): DatabaseBuilder {
            synchronized(DatabaseBuilder::class.java) {
                if(!::instance.isInitialized) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DatabaseBuilder::class.java,
                        "buildDatabase")
                        .build()
                }
            }
            return instance
        }

    }
}