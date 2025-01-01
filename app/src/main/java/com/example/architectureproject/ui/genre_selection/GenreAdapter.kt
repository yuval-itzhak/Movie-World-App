package com.example.architectureproject.ui.genre_selection

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.architectureproject.R
class GenreAdapter(
    private val genres: List<String>,
    private val listener: OnGenreClickListener
) : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

    private var selectedPosition: Int = -1 // Tracks the selected position

    interface OnGenreClickListener {
        fun onGenreClick(genre: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_genre, parent, false)
        return GenreViewHolder(view)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val genre = genres[position]
        holder.genreTextView.text = genre

        // Update background color based on selection
        if (position == selectedPosition) {
            holder.genreTextView.setBackgroundColor(0xFF6200EE.toInt()) // Highlighted color (purple)
            holder.genreTextView.setTextColor(0xFFFFFFFF.toInt()) // White text
        } else {
            holder.genreTextView.setBackgroundColor(0xA9A9A9.toInt()) // Default color (light gray)
            holder.genreTextView.setTextColor(0xFF000000.toInt()) // Black text
        }

        // Handle item click
        holder.itemView.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = position

            // Notify adapter to refresh UI for the updated selection
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)

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
