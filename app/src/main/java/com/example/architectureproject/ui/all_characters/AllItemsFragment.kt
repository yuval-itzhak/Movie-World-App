package com.example.architectureproject.ui.all_characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.core.os.trace
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.architectureproject.R
import com.example.architectureproject.databinding.AllItemsLayoutBinding
import com.example.architectureproject.ui.ItemsViewModel
import il.co.syntax.architectureprojects.Item

class AllItemsFragment : Fragment() {

    private var _binding : AllItemsLayoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel : ItemsViewModel by  activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)

        _binding = AllItemsLayoutBinding.inflate(inflater, container, false)

        binding.fab.setOnClickListener {
            viewModel.clearChosenItem() // Clear any previously selected item
            findNavController().navigate(R.id.action_allItemsFragment_to_addItemFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString("title" )?.let {
            Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
        }

        viewModel.items?.observe(viewLifecycleOwner){
            binding.recycle.adapter = ItemAdapter(it, object : ItemAdapter.ItemListener {
                override fun onItemClicked(position: Int) {
                    Toast.makeText(requireContext(), "${it[position]}", Toast.LENGTH_SHORT).show()
                }

                override fun onItemLongClicked(position: Int) {
                    val item = it[position]
                    viewModel.setItem(item)
                    findNavController().navigate(R.id.action_allItemsFragment_to_detailItemFragment)
                }

                override fun onEditClicked(position: Int) {

                    val item = it[position]
                    viewModel.setItem(item) // Pass the selected item to ViewModel for editing
                    findNavController().navigate(R.id.action_allItemsFragment_to_addItemFragment)

                }
            })
            binding.recycle.layoutManager = LinearLayoutManager(requireContext())
        }



        ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) = makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT)

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //will give me the current item (the item that i did the swap action on him)
                //the adapterPosition will return the index of the item in the items list that representing in the view
                val position = viewHolder.adapterPosition
                val item = (binding.recycle.adapter as ItemAdapter).itemAt(position)
                // הצגת דיאלוג אישור
                AlertDialog.Builder(requireContext())
                    .setTitle("Confirm Deletion")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("Yes") { _, _ ->
                        // אם המשתמש מאשר, מוחקים את הפריט
                        viewModel.deleteItem(item)
                        Toast.makeText(requireContext(), "Item deleted", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("No") { _, _ ->
                        // אם המשתמש לא מאשר, מחזירים את הפריט לרשימה
                        (binding.recycle.adapter as ItemAdapter).notifyItemChanged(position)
                        Toast.makeText(requireContext(), "Item not deleted", Toast.LENGTH_SHORT).show()
                    }
                    .setOnCancelListener {
                        // אם הדיאלוג נסגר ללא פעולה, מחזירים את הפריט לרשימה
                        (binding.recycle.adapter as ItemAdapter).notifyItemChanged(position)
                    }
                    .show()

            }
        }).attachToRecyclerView(binding.recycle)
    }


    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //כדי לקבל את האירוע של הלחיצה על הפח אשפה (על הmenu)
    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_delete){
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Confirm delete")
                .setMessage("Are you sure you want to delete all? ")
                .setPositiveButton("Yes"){
                    p0, p1 ->
                    viewModel.deleteAll()
                    Toast.makeText(requireContext(), "Items deleted", Toast.LENGTH_SHORT).show()
                }.show()
            //p0 - dialog himself , p1 - the id of the button
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}

