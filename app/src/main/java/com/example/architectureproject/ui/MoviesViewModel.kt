package com.example.architectureproject.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.architectureproject.data.repository.MovieRepository
import com.example.architectureproject.data.model.Movie

class MoviesViewModel(application : Application) : AndroidViewModel(application) {

    private val repository = MovieRepository(application)

    val items : LiveData<List<Movie>>? = repository.getMovies()

    fun addMovie(movie : Movie){
        repository.addMovie(movie)
    }
    fun deleteMovie(movie: Movie){
        repository.deleteMovie(movie)
    }
    fun updateMovie(movie: Movie) {
        repository.updateMovie(movie)
    }

    //מאפשר לך לשתף את הנתונים בין ה-Fragments
    private val _chosenMovie = MutableLiveData<Movie?>()
    val chosenMovie : LiveData<Movie?> get() = _chosenMovie

    fun setMovie(movie : Movie){
        _chosenMovie.value = movie
    }

    fun deleteAll(){
        repository.deleteAll()
    }

    fun clearChosenMovie() {
        _chosenMovie.value = null
    }


}