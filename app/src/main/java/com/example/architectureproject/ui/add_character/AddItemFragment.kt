package com.example.architectureproject.ui.add_character

//import GenreDecoration
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.architectureproject.R
import com.example.architectureproject.data.model.Item
import com.example.architectureproject.databinding.AddItemLayoutBinding
import com.example.architectureproject.ui.ItemsViewModel
import com.example.architectureproject.ui.genre_selection.GenreAdapter


class AddItemFragment : Fragment() {



    private var _binding: AddItemLayoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ItemsViewModel by activityViewModels()

    private var imageUri: Uri? = null
    private lateinit var pickImageLauncher: ActivityResultLauncher<Array<String>>

    private lateinit var genreAdapter: GenreAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddItemLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Image Picker
        pickImageLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            binding.resultImage.setImageURI(it)
            if (it != null) {
                requireActivity().contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
            imageUri = it
        }

        // Setup RecyclerView
        setupRecyclerView()

        // Prefill fields if editing an existing item
        val chosenItem = viewModel.chosenItem.value
        chosenItem?.let { item ->
            binding.itemTitle.setText(item.title)
            binding.genreSelection.setText(item.genre)
            binding.itemDirector.setText(item.director)
            binding.itemWriter.setText(item.writer)
            binding.itemStars.setText(item.stars)
            binding.itemRelease.setText(item.release.toString())
            binding.itemDescription.setText(item.description)
            imageUri = item.photo?.let { Uri.parse(it) }
            binding.resultImage.setImageURI(imageUri)
        }

        // Handle Finish button
        binding.finishBtn.setOnClickListener {
            val title = binding.itemTitle.text.toString()
            val genre = binding.genreSelection.text.toString()
            val director = binding.itemDirector.text.toString()
            val writer = binding.itemWriter.text.toString()
            val stars = binding.itemStars.text.toString()
            val release = binding.itemRelease.text.toString().trim()
            val description = binding.itemDescription.text.toString()
            val photo = imageUri?.toString()

            if (title.isEmpty()) {
                Toast.makeText(requireContext(), "Enter movie name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (genre.isEmpty()) {
                Toast.makeText(requireContext(), "Select genre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!director.matches(Regex("^[a-zA-Z\\s]+$"))) {
                Toast.makeText(requireContext(), "Enter director's name properly", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!writer.matches(Regex("^[a-zA-Z\\s]+$"))) {
                Toast.makeText(requireContext(), "Enter writer's name properly", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!stars.matches(Regex("^[a-zA-Z\\s,]+$"))) {
                Toast.makeText(requireContext(), "Enter stars' name properly with comma (,) between each name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!release.matches(Regex("^\\d{4}$"))) {
                Toast.makeText(requireContext(), "Enter year with 4 digits only", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
            try {
                val releaseYear = release.toInt()
                if (releaseYear > currentYear) {
                    Toast.makeText(requireContext(), "Year cannot be in the future", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            } catch (e: NumberFormatException) {
                Toast.makeText(requireContext(), "Enter a valid year", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (description.isEmpty()) {
                Toast.makeText(requireContext(), "Enter movie description", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (photo == null || photo.isEmpty()) {
                Toast.makeText(requireContext(), "Select photo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (chosenItem != null) {
                val updatedItem = chosenItem.copy(
                    title = title,
                    genre = genre,
                    director = director,
                    writer = writer,
                    stars = stars,
                    release = release.toInt(),
                    description = description,
                    photo = photo
                )
                viewModel.updateItem(updatedItem)
            } else {
                val newItem = Item(
                    title = title,
                    genre = genre,
                    director = director,
                    writer = writer,
                    stars = stars,
                    release = release.toInt(),
                    description = description,
                    photo = photo
                )
                viewModel.addItem(newItem)
            }

            viewModel.clearChosenItem()
            findNavController().navigate(R.id.action_addItemFragment_to_allItemsFragment)
        }

        // Handle Image button
        binding.imageBtn.setOnClickListener {
            pickImageLauncher.launch(arrayOf("image/*"))
        }
    }

    private fun setupRecyclerView() {
        val genres = listOf("Action", "Comedy", "Drama", "Sci-Fi", "Horror")

        genreAdapter = GenreAdapter(genres, object : GenreAdapter.OnGenreClickListener {
            override fun onGenreClick(genre: String) {
                binding.genreSelection.setText("Selected genre: $genre")
            }
        })

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = genreAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
