package com.example.architectureproject.ui.genre_selection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.architectureproject.databinding.ItemGenreBinding
//import com.example.myapp.databinding.ItemGenreBinding

class GenreAdapter(
    private val genres: List<String>,
    private val listener: OnGenreClickListener
) : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val binding = ItemGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GenreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val genre = genres[position]
        holder.bind(genre, listener)
    }

    override fun getItemCount(): Int = genres.size

    class GenreViewHolder(private val binding: ItemGenreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(genre: String, listener: OnGenreClickListener) {
            binding.genreTextView.text = genre
            binding.root.setOnClickListener {
                listener.onGenreClick(genre)
            }
        }
    }

    interface OnGenreClickListener {
        fun onGenreClick(genre: String)
    }
}
