package com.example.architectureproject.ui.add_character

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.architectureproject.R
import com.example.architectureproject.databinding.AddItemLayoutBinding
import com.example.architectureproject.ui.ItemsViewModel
import com.example.architectureproject.data.model.Item

class AddItemFragment : Fragment() {

    private var _binding : AddItemLayoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel : ItemsViewModel by  activityViewModels()


    private var imageUri : Uri?= null
    val pickImageLauncher : ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.OpenDocument()){
            binding.resultImage.setImageURI(it)
            if (it != null) {
                requireActivity().contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            imageUri = it
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddItemLayoutBinding.inflate(inflater, container, false)
//        binding.imageBtn.setOnClickListener{
//            pickImageLauncher.launch(arrayOf("image/*"))
//        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Prefill fields if editing an existing item
        val chosenItem = viewModel.chosenItem.value
        chosenItem?.let { item ->
            binding.itemTitle.setText(item.title)
            binding.itemDescription.setText(item.description)
            imageUri = item.photo?.let { Uri.parse(it) }
            binding.resultImage.setImageURI(imageUri)
        }

        // Handle Finish button
        binding.finishBtn.setOnClickListener {
            val title = binding.itemTitle.text.toString()
            val description = binding.itemDescription.text.toString()
            val photo = imageUri?.toString()

            if (chosenItem != null) {
                // Update existing item
                val updatedItem = chosenItem.copy(title = title, description = description, photo = photo)
                viewModel.updateItem(updatedItem)
            } else {
                // Add new item
                val newItem = Item(title = title, description = description, photo = photo)
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}