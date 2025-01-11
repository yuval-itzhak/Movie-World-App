package com.example.movieworldproject.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.movieworldproject.data.local_db.MovieDao
import com.example.movieworldproject.data.local_db.MovieDataBase
import com.example.movieworldproject.data.model.Movie

class MovieRepository(application : Application)  {


    private var movieDao : MovieDao?

    init {
        val db = MovieDataBase.getDataBase(application.applicationContext)
        movieDao = db.movieDao()

    }

    fun getMovies(): LiveData<List<Movie>>? {
        return movieDao?.getMovies()
    }
    fun getMovie(id: Int): Movie?{
        return movieDao?.getMovie(id)
    }

    suspend fun addMovie(movie : Movie){

        movieDao?.addMovie(movie)

    }

    suspend fun deleteMovie(movie : Movie){
        movieDao?.deleteMovie(movie)

    }

    suspend fun updateMovie(movie: Movie){
        movieDao?.update(movie)
    }

    suspend fun deleteAll() {
        movieDao?.deleteAll()
    }

}