package com.example.gamingwakeup.model.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gamingwakeup.model.data.entity.AlarmEntity

@Database(entities = [AlarmEntity::class], version = 1)
abstract class AlarmDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao

    companion object {
        private const val DATABASE_NAME = "alarm_database"
        private lateinit var DATABASE_INSTANCE: AlarmDatabase

        fun getInstance(context: Context): AlarmDatabase {
            synchronized(AlarmDatabase::class.java) {
                if (!Companion::DATABASE_INSTANCE.isInitialized) {
                    DATABASE_INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AlarmDatabase::class.java,
                        DATABASE_NAME
                    ).build()
                }
            }
            return DATABASE_INSTANCE
        }
    }
}
