package com.example.tvshowsapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tvshowsapp.dao.TvShowDao
import com.example.tvshowsapp.models.TvShow

@Database(entities = [TvShow::class], version = 1, exportSchema = false)
abstract class TvShowDatabase : RoomDatabase() {

    companion object {
        private var tvShowDatabase: TvShowDatabase? = null

        fun getTvShowsDatabase(context: Context): TvShowDatabase? {
            if (tvShowDatabase == null) {
                tvShowDatabase =
                    Room.databaseBuilder(context, TvShowDatabase::class.java, "tv_shows_db").build()
            }
            return tvShowDatabase
        }
    }

    abstract fun tvShowDao(): TvShowDao
}