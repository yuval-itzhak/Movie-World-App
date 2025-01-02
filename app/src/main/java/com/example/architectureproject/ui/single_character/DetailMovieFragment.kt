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

    val viewModel : MoviesViewModel by activityViewModels()
    var _binding : DetailMovieLayoutBinding? = null

    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailMovieLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.chosenMovie.observe(viewLifecycleOwner){//it:movie
            binding.movieTitle.text = it?.title
            binding.movieDescription.text = it?.description
            Glide.with(requireContext()).load(it?.photo).into(binding.movieImage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}