package com.example.architectureproject.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.architectureproject.data.repository.ItemRepository
import com.example.architectureproject.data.model.Item

class ItemsViewModel(application : Application) : AndroidViewModel(application) {

    private val repository = ItemRepository(application)

    val items : LiveData<List<Item>>? = repository.getItems()

    fun addItem(item : Item){
        repository.addItem(item)
    }
    fun deleteItem(item: Item){
        repository.deleteItem(item)
    }
    fun updateItem(item: Item) {
        repository.updateItem(item)
    }

    //מאפשר לך לשתף את הנתונים בין ה-Fragments
    private val _chosenItem = MutableLiveData<Item?>()
    val chosenItem : LiveData<Item?> get() = _chosenItem

    fun setItem(item : Item){
        _chosenItem.value = item
    }

    fun deleteAll(){
        repository.deleteAll()
    }

    fun clearChosenItem() {
        _chosenItem.value = null
    }


}