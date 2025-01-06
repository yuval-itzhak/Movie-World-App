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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

class DetailMovieFragment : Fragment() {

    private val viewModel: MoviesViewModel by activityViewModels()
    private var _binding: DetailMovieLayoutBinding? = null
    private val binding get() = _binding!!

    private var videoId: String? = null

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

        viewModel.chosenMovie.observe(viewLifecycleOwner) { movie ->
            if (movie != null) {
                binding.movieTitle.text = "${movie.title}"
                binding.movieGenre.text = "Genre: ${movie.genre}"
                binding.movieDirector.text = "Director: ${movie.director}"
                binding.movieWriter.text = "Writer: ${movie.writer}"
                binding.movieStars.text = "Stars: ${movie.stars}"
                binding.movieRelease.text = "Release year: ${movie.release}"
                binding.movieDescription.text = movie.description
                Glide.with(requireContext()).load(movie.photo).into(binding.movieImage)
                videoId = movie.videoId

                if (videoId.isNullOrEmpty()) { //Toggle youtube player visibility based on the value of videoId
                    binding.youtubePlayerView.visibility = View.GONE
                } else {
                    binding.youtubePlayerView.visibility = View.VISIBLE
                }
            }
        }

        val youTubePlayerView = binding.youtubePlayerView
        lifecycle.addObserver(youTubePlayerView)
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                videoId?.let {
                    youTubePlayer.loadVideo(it, 0f)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
