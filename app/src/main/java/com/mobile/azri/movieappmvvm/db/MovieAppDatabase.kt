package com.mobile.azri.movieappmvvm.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mobile.azri.movieappmvvm.di.ApplicationScope
import com.mobile.azri.movieappmvvm.model.Movie
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Provider

//TODO 19 - Create MovieAppDatabase for db local
@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class MovieAppDatabase : RoomDatabase() {
    abstract fun getMovieAppDao(): MovieDao

    class Callback @Inject constructor(
        private val database: Provider<MovieAppDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback()
}