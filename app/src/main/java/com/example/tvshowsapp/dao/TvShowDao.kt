package com.example.tvshowsapp.dao

import androidx.room.*
import com.example.tvshowsapp.models.TvShow
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface TvShowDao {

    @Query("SELECT * from tvShows")
    fun getWatchList(): Flowable<List<TvShow>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addToWatchList(tvShow: TvShow): Completable

    @Delete
    fun removeFromWatchList(tvShow: TvShow): Completable

    @Query("SELECT * From tvShows where id = :tvShowId")
    fun getTvShowFromWatchlist(tvShowId: String): Flowable<TvShow>
}