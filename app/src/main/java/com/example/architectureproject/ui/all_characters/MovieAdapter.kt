package com.example.movieworldproject.ui.all_characters

import androidx.recyclerview.widget.RecyclerView
import com.example.movieworldproject.databinding.MovieLayoutBinding
import com.example.movieworldproject.data.model.Movie
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide


class MovieAdapter(val movies: List<Movie>, val callBack: MovieListener) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var filteredMovies: MutableList<Movie> = movies.toMutableList()

    interface MovieListener {
        fun onMovieLongClicked(position: Int)
        fun onEditClicked(position: Int)
    }


    inner class MovieViewHolder(private val binding: MovieLayoutBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnLongClickListener {

        init {
            binding.root.setOnLongClickListener(this)

            binding.editButton.setOnClickListener {
                callBack.onEditClicked(adapterPosition)
            }
        }

        override fun onLongClick(p0: View?): Boolean {
            callBack.onMovieLongClicked(adapterPosition)
            return true
        }

        fun bind(movie: Movie) {
            binding.movieName.text = movie.title
            binding.movieYear.text = movie.release.toString()
            binding.movieGenre.text = movie.genre
            Glide.with(binding.root).load(movie.photo).circleCrop().into(binding.movieImage)
        }
    }

    fun movieAt(position: Int) = filteredMovies[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MovieViewHolder(
            MovieLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) =
        holder.bind(filteredMovies[position])

    override fun getItemCount() =
        filteredMovies.size

    fun filterByTitle(query: String) {
        filteredMovies = if (query.isEmpty()) {
            movies.toMutableList()
        } else {
            movies.filter { it.title.contains(query, ignoreCase = true) }.toMutableList()
        }
        notifyDataSetChanged()
    }

    fun filterByGenre(genre: String) {
        filteredMovies = if (genre == "All" || genre == "הכל") {
            movies.toMutableList()
        } else {
            movies.filter { it.genre == genre }.toMutableList()
        }
        notifyDataSetChanged()
    }


}




