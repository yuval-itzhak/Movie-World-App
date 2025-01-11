package com.example.movieworldproject.ui.add_character

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieworldproject.R
import com.example.movieworldproject.data.model.Movie
import com.example.movieworldproject.databinding.AddMovieLayoutBinding
import com.example.movieworldproject.ui.MoviesViewModel
import com.example.movieworldproject.ui.genre_selection.GenreAdapter
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.EditText

class AddMovieFragment : Fragment() {

    private var _binding: AddMovieLayoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MoviesViewModel by activityViewModels()

    private var imageUri: Uri? = null
    private lateinit var pickImageLauncher: ActivityResultLauncher<Array<String>>

    private lateinit var genreAdapter: GenreAdapter

    private lateinit var textView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddMovieLayoutBinding.inflate(inflater, container, false)
        textView = binding.newOrEditMovie

        val newText = arguments?.getString("add/edit")

        newText?.let {
            textView.text = it
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Image Picker
        pickImageLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                imageUri = uri
                binding.resultImage.setImageURI(uri)
                binding.resultImage.visibility = View.VISIBLE // Show image when selected
                requireActivity().contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
        }

        // Initially hide the ImageView
        binding.resultImage.visibility = View.INVISIBLE

        setupRecyclerView()
        setupBulletInput(binding.movieStars)

        // Prefill fields if editing an existing item
        val chosenMovie = viewModel.chosenMovie.value
        chosenMovie?.let { movie ->
            binding.movieTitle.setText(movie.title)
            binding.genreSelection.setText(movie.genre)
            binding.movieDirector.setText(movie.director)
            binding.movieWriter.setText(movie.writer)
            binding.movieStars.setText(movie.stars)
            binding.movieRelease.setText(movie.release.toString())
            binding.movieDescription.setText(movie.description)

            // Set the existing poster if available
            imageUri = movie.photo?.let { Uri.parse(it) }
            if (imageUri != null) {
                binding.resultImage.setImageURI(imageUri)
                binding.resultImage.visibility = View.VISIBLE // Ensure visibility for existing poster
            }

            binding.videoIdInput.setText(movie.videoId)
        }

        // Handle Finish button
        binding.finishBtn.setOnClickListener {
            val title = binding.movieTitle.text.toString()
            val genre = binding.genreSelection.text.toString()
            val director = binding.movieDirector.text.toString()
            val writer = binding.movieWriter.text.toString()
            val stars = binding.movieStars.text.toString()
            val release = binding.movieRelease.text.toString().trim()
            val description = binding.movieDescription.text.toString()
            val photo = imageUri?.toString()
            val input = binding.videoIdInput.text.toString().trim()
            val videoId = input.takeIf { it.isNotEmpty() }?.let {
                val regex = "(?<=v=|youtu\\.be/)[^&?\\s]+".toRegex()
                regex.find(it)?.value
            }


            // Validation
            if (title.isEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.alert_enter_movie_name), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (genre.isEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.alert_select_genre), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!director.matches(Regex("^[a-zA-Zא-ת\\s]+$"))) {
                Toast.makeText(requireContext(), getString(R.string.alert_director_properly), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!writer.matches(Regex("^[a-zA-Zא-ת\\s]+$"))) {
                Toast.makeText(requireContext(), getString(R.string.alert_writer_properly), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!release.matches(Regex("^\\d{4}$"))) {
                Toast.makeText(requireContext(), getString(R.string.alert_year_4_digits), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
            try {
                val releaseYear = release.toInt()
                if (releaseYear > currentYear || releaseYear < 1900) {
                    Toast.makeText(requireContext(), getString(R.string.alert_year_between), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            } catch (e: NumberFormatException) {
                Toast.makeText(requireContext(), getString(R.string.alert_valid_year), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (stars.isEmpty()) {
                Toast.makeText(requireContext(),
                    getString(R.string.enter_stars_names), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!stars.matches(Regex("^['a-zA-Zא-ת•\\s]+$"))) {
                Toast.makeText(requireContext(),
                    getString(R.string.enter_stars_names_properly), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (description.isEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.alert_movie_description), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (photo == null || photo.isEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.alert_select_poster), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (chosenMovie != null) {
                val updatedMovie = chosenMovie.copy(
                    title = title,
                    genre = genre,
                    director = director,
                    writer = writer,
                    stars = stars,
                    release = release.toInt(),
                    description = description,
                    photo = photo,
                    videoId = videoId
                )
                viewModel.updateMovie(updatedMovie)
            } else {
                val newMovie = Movie(
                    title = title,
                    genre = genre,
                    director = director,
                    writer = writer,
                    stars = stars,
                    release = release.toInt(),
                    description = description,
                    photo = photo,
                    videoId = videoId
                )
                viewModel.addMovie(newMovie)
            }

            viewModel.clearChosenMovie()
            findNavController().navigate(R.id.action_addMovieFragment_to_allMoviesFragment)
        }

        // Handle Image button
        binding.imageBtn.setOnClickListener {
            pickImageLauncher.launch(arrayOf("image/*"))
        }
    }

    private fun setupRecyclerView() {
        val genres = listOf(
            getString(R.string.action),
            getString(R.string.comedy),
            getString(R.string.drama),
            getString(R.string.fantasy),
            getString(R.string.sci_fi),
            getString(R.string.horror),
            getString(R.string.romantic),
            getString(R.string.documentary),
            getString(R.string.musical),
            getString(R.string.other)
        )

        val selectedGenre = viewModel.chosenMovie.value?.genre // Get the selected genre for editing

        genreAdapter = GenreAdapter(genres, object : GenreAdapter.OnGenreClickListener {
            override fun onGenreClick(genre: String) {
                binding.genreSelection.setText(genre) // Update the selected genre in the input field
            }
        }, selectedGenre)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = genreAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupBulletInput(editText: EditText) {
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && editText.text.isEmpty()) {
                editText.setText("• ")
                editText.setSelection(editText.text.length)
            } else if (!hasFocus && editText.text.toString().trim() == "•") {
                editText.text.clear()
            }
        }

        editText.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                val cursorPosition = editText.selectionStart
                val text = editText.text.toString()

                when (keyCode) {
                    KeyEvent.KEYCODE_ENTER -> {
                        if (cursorPosition >= 0 && cursorPosition <= text.length) {
                            val lineBreak = "\n• "
                            editText.text.insert(cursorPosition, lineBreak)
                            editText.setSelection(cursorPosition + lineBreak.length)
                            return@setOnKeyListener true
                        }
                    }

                    KeyEvent.KEYCODE_DEL -> {
                        if (cursorPosition > 2 && text.substring(
                                cursorPosition - 2,
                                cursorPosition
                            ) == "• "
                        ) {
                            editText.text.delete(cursorPosition - 2, cursorPosition)
                            return@setOnKeyListener true
                        }
                    }
                }
            }
            false
        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                if (text.isNotEmpty()) {
                    val lines = text.split("\n")
                    val formattedText = lines.joinToString("\n") { line ->
                        if (line.isNotBlank() && !line.trim().startsWith("•")) {
                            "• ${line.trim()}"
                        } else {
                            line
                        }
                    }

                    if (formattedText != text) {
                        editText.removeTextChangedListener(this)
                        editText.setText(formattedText)
                        editText.setSelection(editText.text.length)
                        editText.addTextChangedListener(this)
                    }
                }
            }
        })
    }
}
