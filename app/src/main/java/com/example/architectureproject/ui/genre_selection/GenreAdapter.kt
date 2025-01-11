package com.example.movieworldproject.ui.genre_selection

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieworldproject.R

class GenreAdapter(
    private val genres: List<String>,
    private val listener: OnGenreClickListener,
    private val initialSelectedGenre: String? = null
) : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

    private var selectedPosition: Int = genres.indexOf(initialSelectedGenre)

    interface OnGenreClickListener {
        fun onGenreClick(genre: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_genre, parent, false)
        return GenreViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: GenreViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val genre = genres[position]
        holder.genreTextView.text = genre

        // Default styling for unselected items
        holder.genreTextView.setBackgroundColor(0xFFFDE7.toInt())
        holder.genreTextView.setTextColor(0xFF000000.toInt())

        // Highlight selected item
        if (position == selectedPosition) {
            holder.genreTextView.setBackgroundColor(0xFFC6A700.toInt())
            holder.genreTextView.setTextColor(0xFF000000.toInt())
        }

        // Handle click event to update selection
        holder.itemView.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = position

            // Notify RecyclerView to update the item views
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)

            // Notify listener about the genre selection
            listener.onGenreClick(genre)
        }
    }

    override fun getItemCount(): Int {
        return genres.size
    }

    class GenreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val genreTextView: TextView = itemView.findViewById(R.id.genreTextView)
    }
}
