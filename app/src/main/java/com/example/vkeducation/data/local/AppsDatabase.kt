package com.example.vkeducation.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(
    entities = [AppShortDbModel::class, AppDetailsDbModel::class],
    version = 3,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class AppsDatabase : RoomDatabase() {

    abstract fun appsDao(): AppsDao

    companion object {
        private var instance: AppsDatabase? = null
        private val LOCK = Any()

        fun getInstance(context: Context): AppsDatabase {
            instance?.let { return it }
            synchronized(LOCK) {
                return Room.databaseBuilder(
                    context = context,
                    klass = AppsDatabase::class.java,
                    name = "apps.db"
                ).build().also {
                    instance = it
                }
            }
        }
    }

}