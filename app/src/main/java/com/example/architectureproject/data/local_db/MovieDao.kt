package com.example.architectureproject.data.local_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.architectureproject.data.model.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMovie(movie: Movie)

    @Delete
    fun deleteMovie(movie: Movie)

    @Update
    fun update(movie: Movie)

    @Query("SELECT * FROM movies ORDER BY content ASC")
    fun getMovies(): LiveData<List<Movie>>

    // Fetch by primary key (id)
    @Query("SELECT * FROM movies WHERE id = :id")
    fun getMovie(id: Int): Movie

    // Search by content (optional)
    @Query("SELECT * FROM movies WHERE content LIKE '%' || :content || '%'")
    fun getMovieByContent(content: String): List<Movie>

    @Query("DELETE FROM movies")
    fun deleteAll()
}
