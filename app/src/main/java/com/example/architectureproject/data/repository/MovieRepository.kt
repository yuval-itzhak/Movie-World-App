package com.example.architectureproject.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.architectureproject.data.local_db.MovieDao
import com.example.architectureproject.data.local_db.MovieDataBase
import com.example.architectureproject.data.model.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.launch

class MovieRepository(application : Application)  {// : CoroutineScope {

//    override val coroutineContext: CoroutineContext
//        get() = Dispatchers.IO

    private var movieDao : MovieDao?

    init {
        val db = MovieDataBase.getDataBase(application.applicationContext)
        movieDao = db?.movieDao()

    }

    fun getMovies(): LiveData<List<Movie>>? {
        return movieDao?.getMovies()
    }
    fun getMovie(id: Int): Movie?{
        return movieDao?.getMovie(id)
    }

    suspend fun addMovie(movie : Movie){
       // launch {
            movieDao?.addMovie(movie)
       // }
    }

    suspend fun deleteMovie(movie : Movie){
       // launch {
            movieDao?.deleteMovie(movie)
       // }
    }

    suspend fun updateMovie(movie: Movie){
        //launch {
            movieDao?.update(movie)
        //}
    }

    suspend fun deleteAll() {
//        launch {
            movieDao?.deleteAll()
//        }
    }

}