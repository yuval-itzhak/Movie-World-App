package com.example.movieworldproject.ui.all_characters

import android.graphics.Color
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieworldproject.R
import com.example.movieworldproject.databinding.AllMoviesLayoutBinding
import com.example.movieworldproject.ui.MoviesViewModel
import com.example.movieworldproject.ui.genre_selection.GenreAdapter
import com.google.android.material.snackbar.Snackbar

@Suppress("DEPRECATION")
class AllMoviesFragment : Fragment() {

    private var _binding: AllMoviesLayoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MoviesViewModel by activityViewModels()
    lateinit var adapter: MovieAdapter
    private lateinit var genreAdapter: GenreAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = AllMoviesLayoutBinding.inflate(inflater, container, false)

        // Set up RecyclerView
        setupGenreRecyclerView()
        setupSearchBar()
        setHasOptionsMenu(true)

        binding.fab.setOnClickListener {
            viewModel.clearChosenMovie()
            findNavController().navigate(R.id.action_allMoviesFragment_to_addMovieFragment)
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        viewModel.items?.observe(viewLifecycleOwner) { movieList ->
            adapter = MovieAdapter(movieList, object : MovieAdapter.MovieListener {


                override fun onMovieClicked(position: Int) {
                    val movie = movieList[position]
                    viewModel.setMovie(movie)
                    findNavController().navigate(R.id.action_allMoviesFragment_to_detailMovieFragment)
                }

                override fun onEditClicked(position: Int) {
                    val movie = movieList[position]
                    val bundle = Bundle().apply {
                        putString("add/edit", getString(R.string.edit_movie))
                    }
                    viewModel.setMovie(movie)
                    findNavController().navigate(
                        R.id.action_allMoviesFragment_to_addMovieFragment,
                        bundle
                    )
                }
            })
            binding.recycle.adapter = adapter
            binding.recycle.layoutManager = LinearLayoutManager(requireContext())
        }

        ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) = makeFlag(
                ItemTouchHelper.ACTION_STATE_SWIPE,
                ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
            )

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val movie = (binding.recycle.adapter as MovieAdapter).movieAt(position)

                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.confirm_deletion))
                    .setMessage(getString(R.string.are_you_sure))
                    .setPositiveButton(getString(R.string.yes)) { _, _ ->
                        viewModel.deleteMovie(movie)
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.movie_deleted), Toast.LENGTH_SHORT
                        ).show()
                    }
                    .setNegativeButton(getString(R.string.no)) { _, _ ->
                        (binding.recycle.adapter as MovieAdapter).notifyItemChanged(position)
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.movie_not_deleted), Toast.LENGTH_SHORT
                        ).show()
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
                if (s != null && this@AllMoviesFragment::adapter.isInitialized) {
                    adapter.filterByTitle(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupGenreRecyclerView() {
        // Ensure getString is called after the Fragment is attached to its context
        val genres = resources.getStringArray(R.array.movie_genres).toList()

        val selectedGenre = viewModel.chosenMovie.value?.genre // Get the selected genre for editing

        genreAdapter = GenreAdapter(genres, object : GenreAdapter.OnGenreClickListener {
            override fun onGenreClick(genre: String) {
                // Handle genre selection and filter movies based on the selected genre
                adapter.filterByGenre(genre)
            }
        }, selectedGenre)

        binding.recyclerViewFilter.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = genreAdapter
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.info_button) {
            val snackbar = Snackbar.make(
                binding.root,
                getString(R.string.swipe_for_delete_a_movie_click_for_more_details),
                Snackbar.LENGTH_INDEFINITE // Use INDEFINITE duration
            )

            snackbar.setAction(getString(R.string.dismiss)) {
                snackbar.dismiss()
            }

            // Modify the layout parameters to position the Snackbar in the middle
            val snackbarView = snackbar.view
            val params = snackbarView.layoutParams

            // Check the type of layout parameters and cast accordingly
            if (params is CoordinatorLayout.LayoutParams) {
                params.gravity = Gravity.START
            } else if (params is FrameLayout.LayoutParams) {
                params.gravity = Gravity.START
            }
            snackbarView.layoutParams = params
            snackbar.setActionTextColor(Color.WHITE)
            snackbar.view.setBackgroundColor(Color.BLACK)

            // Customize the text appearance
            val snackbarText =
                snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            snackbarText.maxLines = 4
            snackbarText.textAlignment = View.TEXT_ALIGNMENT_CENTER
            snackbarText.setTextColor(Color.WHITE)
            snackbarText.textSize = 14f


            // Auto-dismiss after a delay
            val handler = Handler()
            handler.postDelayed({
                snackbar.dismiss()
            }, 5000)
            snackbar.show()

        }
        if (item.itemId == R.id.action_delete) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(getString(R.string.confirm_deletion))
                .setMessage(getString(R.string.are_you_sure_you_want_to_delete_all))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    viewModel.deleteAll()
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.movies_deleted), Toast.LENGTH_SHORT
                    ).show()
                }.show()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
