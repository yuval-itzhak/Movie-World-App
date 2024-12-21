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
import il.co.syntax.architectureprojects.Item

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
        binding.finishBtn.setOnClickListener{
//            val bundle = bundleOf("title" to binding.itemTitle.text.toString(),
//                "description" to binding.itemDescription.text.toString() )
//            findNavController().navigate(R.id.action_addItemFragment_to_allItemsFragment, bundle)

            val item = Item(binding.itemTitle.text.toString(), binding.itemDescription.text.toString(),
                imageUri.toString())

            //ItemManager.add(item)
            viewModel.addItem(item)
            findNavController().navigate(R.id.action_addItemFragment_to_allItemsFragment)

        }
        binding.imageBtn.setOnClickListener{
            pickImageLauncher.launch(arrayOf("image/*"))
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}