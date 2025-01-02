package com.example.architectureproject.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.architectureproject.data.repository.MovieRepository
import com.example.architectureproject.data.model.Movie
import kotlinx.coroutines.launch

class MoviesViewModel(application : Application) : AndroidViewModel(application) {

    private val repository = MovieRepository(application)

    val items : LiveData<List<Movie>>? = repository.getMovies()

    fun addMovie(movie : Movie){
        viewModelScope.launch{
            repository.addMovie(movie)
        }
    }
    fun deleteMovie(movie: Movie){
        viewModelScope.launch {
            repository.deleteMovie(movie)
        }
    }
    fun updateMovie(movie: Movie) {
        viewModelScope.launch {
            repository.updateMovie(movie)
        }
    }

    fun deleteAll(){
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

    //מאפשר לך לשתף את הנתונים בין ה-Fragments
    private val _chosenMovie = MutableLiveData<Movie?>()
    val chosenMovie : LiveData<Movie?> get() = _chosenMovie

    fun setMovie(movie : Movie){
        _chosenMovie.value = movie
    }


    fun clearChosenMovie() {
        _chosenMovie.value = null
    }


}