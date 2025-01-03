package com.example.architectureproject.ui.all_characters
//android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.architectureproject.R
import com.example.architectureproject.databinding.AllMoviesLayoutBinding
import com.example.architectureproject.ui.MoviesViewModel
import com.google.android.material.snackbar.Snackbar

class AllMoviesFragment : Fragment() {

    private var _binding: AllMoviesLayoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MoviesViewModel by activityViewModels()
    private lateinit var adapter: MovieAdapter // Adapter with filtering capability


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)

        _binding = AllMoviesLayoutBinding.inflate(inflater, container, false)

        setupSearchBar()



        binding.fab.setOnClickListener {
            viewModel.clearChosenMovie() // Clear any previously selected item
            findNavController().navigate(R.id.action_allMoviesFragment_to_addMovieFragment)
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        arguments?.getString("title")?.let {
//            Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show() TODO: delete commented code before submission (part of the requirements)
//        }

        viewModel.items?.observe(viewLifecycleOwner) { movieList ->
            adapter = MovieAdapter(movieList, object : MovieAdapter.MovieListener {
//                override fun onItemClicked(position: Int) {
//                    Toast.makeText(requireContext(), "${itemList[position]}", Toast.LENGTH_SHORT).show()
//                }

                override fun onMovieLongClicked(position: Int) {
                    val movie = movieList[position]
                    viewModel.setMovie(movie)
                    findNavController().navigate(R.id.action_allMoviesFragment_to_detailMovieFragment)
                }

                override fun onEditClicked(position: Int) {
                    val movie = movieList[position]
                    val bundle = Bundle().apply {
                        putString("keyText", "Edit Movie:")
                    }
                    viewModel.setMovie(movie) // Pass the selected item to ViewModel for editing
                    findNavController().navigate(R.id.action_allMoviesFragment_to_addMovieFragment, bundle)
                }
            })
            binding.recycle.adapter = adapter
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
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val movie = (binding.recycle.adapter as MovieAdapter).movieAt(position)

                AlertDialog.Builder(requireContext())
                    .setTitle("Confirm Deletion")
                    .setMessage("Are you sure you want to delete this movie?")
                    .setPositiveButton("Yes") { _, _ ->
                        viewModel.deleteMovie(movie)
                        Toast.makeText(requireContext(), "Movie deleted", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("No") { _, _ ->
                        (binding.recycle.adapter as MovieAdapter).notifyItemChanged(position)
                        Toast.makeText(requireContext(), "Movie not deleted", Toast.LENGTH_SHORT).show()
                    }
                    .setOnCancelListener {
                        (binding.recycle.adapter as MovieAdapter).notifyItemChanged(position)
                    }
                    .show()
            }
        }).attachToRecyclerView(binding.recycle)
    }

    private fun setupSearchBar() {
        binding.searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filterByTitle(s.toString()) // Call the filter function of the adapter
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }



    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //כדי לקבל את האירוע של הלחיצה על הפח אשפה (על הmenu)
    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.info_button){
            val snackbar = Snackbar.make(
                binding.root,
                "Swipe for delete a movie\nLong click for more details",
                Snackbar.LENGTH_INDEFINITE // Use INDEFINITE duration
            )
            snackbar.setAction("DISMISS") {
                snackbar.dismiss() // Allow manual dismissal
            }
            // Auto-dismiss after a delay (e.g., 5 seconds)
            val handler = android.os.Handler()
            handler.postDelayed({
                snackbar.dismiss()
            }, 5000) // 5000 ms = 5 seconds
            snackbar.show()

        }
        if (item.itemId == R.id.action_delete){
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Confirm delete")
                .setMessage("Are you sure you want to delete all? ")
                .setPositiveButton("Yes"){
                    p0, p1 ->
                    viewModel.deleteAll()
                    Toast.makeText(requireContext(), "Movies deleted", Toast.LENGTH_SHORT).show()
                }.show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
