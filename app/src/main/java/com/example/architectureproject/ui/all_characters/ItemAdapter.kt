package com.example.architectureproject.ui.all_characters

import androidx.recyclerview.widget.RecyclerView
import com.example.architectureproject.databinding.ItemLayoutBinding
import com.example.architectureproject.data.model.Item
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide


class ItemAdapter(val items:List<Item>, val callBack: ItemListener)
    : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>(){

    interface ItemListener {
//        fun onItemClicked(position: Int)
        fun onItemLongClicked(position: Int)
        fun onEditClicked(position: Int)
    }

    private var filteredItems: MutableList<Item> = items.toMutableList() // Data source for RecyclerView

    inner class ItemViewHolder(private val binding : ItemLayoutBinding)
        : RecyclerView.ViewHolder(binding.root), View.OnLongClickListener{

            init {
//                binding.root.setOnClickListener(this)
                binding.root.setOnLongClickListener(this)

                binding.editButton.setOnClickListener {
                    callBack.onEditClicked(adapterPosition)
                }
            }


//        override fun onClick(p0: View?) {
//            callBack.onItemClicked(adapterPosition)
//        }

        override fun onLongClick(p0: View?): Boolean {
            callBack.onItemLongClicked(adapterPosition)
            return true
        }

        fun bind(item : Item){
                binding.movieName.text = item.title
                binding.movieYear.text = item.release.toString()
                binding.movieGenre.text = item.genre
                Glide.with(binding.root).load(item.photo).circleCrop().into(binding.itemImage)
            }
        }

    fun itemAt(position : Int) = filteredItems[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(ItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) =
        holder.bind(filteredItems[position])

    override fun getItemCount() =
        filteredItems.size

    // Method to filter movies based on a query
    fun filterByTitle(query: String) {
        filteredItems = if (query.isEmpty()) {
            items.toMutableList()
        } else {
            items.filter { it.title.contains(query, ignoreCase = true) }.toMutableList()
        }
        notifyDataSetChanged()
    }
}




