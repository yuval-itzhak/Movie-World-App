package com.example.architectureproject.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.architectureproject.data.local_db.ItemDao
import com.example.architectureproject.data.local_db.ItemDataBase
import il.co.syntax.architectureprojects.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.launch

class ItemRepository(application : Application) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    private var itemDao : ItemDao?

    init {
        val db = ItemDataBase.getDataBase(application.applicationContext)
        itemDao = db?.itemDao()

    }

    fun getItems(): LiveData<List<Item>>? {
        return itemDao?.getItems()
    }
    fun getItem(id: Int): Item?{
        return itemDao?.getItem(id)
    }

    fun addItem(item : Item){
        launch {
            itemDao?.addItem(item)
        }
    }

    fun deleteItem(item : Item){
        launch {
        itemDao?.deleteItem(item)
        }
    }

    fun updateItem(item: Item){
        launch {
        itemDao?.update(item)
        }
    }

    fun deleteAll() {
        launch {
        itemDao?.deleteAll()
        }
    }

    
}