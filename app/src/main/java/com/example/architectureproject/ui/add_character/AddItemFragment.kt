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
            val release = binding.itemRelease.text.toString().toIntOrNull() ?: 0
            val description = binding.itemDescription.text.toString()
            val photo = imageUri?.toString()


            if (chosenItem != null) {
                // Update existing item
                val updatedItem = chosenItem.copy(
                    title = title,
                    genre = genre,
                    director = director,
                    writer = writer,
                    stars = stars,
                    release = release,
                    description = description,
                    photo = photo
                )
                viewModel.updateItem(updatedItem)
            } else {
                // Add new item
                val newItem = Item(
                    title = title,
                    genre = genre,
                    director = director,
                    writer = writer,
                    stars = stars,
                    release = release,
                    description = description,
                    photo = photo
                )
                viewModel.addItem(newItem)
            }

            // Clear chosen item after finishing
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
