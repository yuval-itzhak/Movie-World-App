package com.example.movieworldproject.ui.single_character

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.movieworldproject.databinding.DetailMovieLayoutBinding
import com.example.movieworldproject.ui.MoviesViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.example.movieworldproject.R

class DetailMovieFragment : Fragment() {

    private val viewModel: MoviesViewModel by activityViewModels()
    private var _binding: DetailMovieLayoutBinding? = null
    private val binding get() = _binding!!

    private var videoId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailMovieLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.chosenMovie.observe(viewLifecycleOwner) { movie ->
            if (movie != null) {
                binding.movieTitle.text = movie.title
                binding.movieGenre.text = Html.fromHtml(
                    getString(R.string.movie_genre, movie.genre),
                    Html.FROM_HTML_MODE_LEGACY
                )
                binding.movieDirector.text = Html.fromHtml(
                    getString(R.string.movie_director, movie.director),
                    Html.FROM_HTML_MODE_LEGACY
                )
                binding.movieWriter.text = Html.fromHtml(
                    getString(R.string.movie_writer, movie.writer),
                    Html.FROM_HTML_MODE_LEGACY
                )
                binding.movieStars.text = Html.fromHtml(
                    getString(R.string.movie_stars, movie.stars),
                    Html.FROM_HTML_MODE_LEGACY
                )
                binding.movieRelease.text = Html.fromHtml(
                    getString(R.string.movie_release_year, movie.release),
                    Html.FROM_HTML_MODE_LEGACY
                )
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
