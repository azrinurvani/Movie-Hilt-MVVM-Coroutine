package com.mobile.azri.movieappmvvm.di

import android.app.Application
import androidx.room.Room
import com.mobile.azri.movieappmvvm.db.MovieAppDatabase
import com.mobile.azri.movieappmvvm.db.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//TODO 21 - Create module for database
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application, callback: MovieAppDatabase.Callback): MovieAppDatabase{
        return Room.databaseBuilder(application, MovieAppDatabase::class.java, "alpha_database")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()
    }

    @Provides
    fun provideMovieAppDao(db: MovieAppDatabase): MovieDao {
        return db.getMovieAppDao()
    }
}