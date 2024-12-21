package com.example.architectureproject.ui.single_character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.architectureproject.databinding.DetailItemLayoutBinding
import com.example.architectureproject.ui.ItemsViewModel


class DetailItemFragment : Fragment() {

    val viewModel : ItemsViewModel by activityViewModels()
    var _binding : DetailItemLayoutBinding? = null

    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailItemLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.chosenItem.observe(viewLifecycleOwner){//it:item
            binding.itemTitle.text = it.title
            binding.itemDescription.text = it.description
            Glide.with(requireContext()).load(it.photo).circleCrop().into(binding.itemImage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}