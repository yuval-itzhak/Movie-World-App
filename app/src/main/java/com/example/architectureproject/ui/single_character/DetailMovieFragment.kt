package com.example.architectureproject.ui.single_character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.architectureproject.databinding.DetailMovieLayoutBinding
import com.example.architectureproject.ui.MoviesViewModel


class DetailMovieFragment : Fragment() {

    val viewModel : MoviesViewModel by activityViewModels() //TODO: take care of the warnings (check if it works when those variables are private)
    var _binding : DetailMovieLayoutBinding? = null

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailMovieLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //TODO: Add animations
        super.onViewCreated(view, savedInstanceState)
        viewModel.chosenMovie.observe(viewLifecycleOwner){
            binding.movieTitle.text = "Title: ${it?.title}" //TODO: take care of the warnings (add string resource file with translations)
            binding.movieGenre.text = "Genre: ${it?.genre}"
            binding.movieDirector.text = "Director: ${it?.director}"
            binding.movieWriter.text = "Writer: ${it?.writer}"
            binding.movieStars.text = "Stars: ${it?.stars}"
            binding.movieRelease.text = "Release year: ${it?.release.toString()}"
            binding.movieDescription.text = it?.description
            Glide.with(requireContext()).load(it?.photo).into(binding.movieImage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}