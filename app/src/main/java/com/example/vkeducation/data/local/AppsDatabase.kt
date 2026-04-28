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

}