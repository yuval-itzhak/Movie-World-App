package com.example.architectureproject.data.repository

import android.app.Application
import com.example.architectureproject.data.local_db.ItemDao
import com.example.architectureproject.data.local_db.ItemDataBase
import il.co.syntax.architectureprojects.Item

class ItemRepository(application : Application) {

    private var itemDao : ItemDao?

    init {
        val db = ItemDataBase.getDataBase(application.applicationContext)
        itemDao = db?.itemDao()

    }

    fun getItems() = itemDao?.getItems()

    fun addItem(item : Item){
        itemDao?.addItem(item)
    }

    fun deleteItem(item : Item){
        itemDao?.deleteItem(item)
    }

    fun getItem(id: Int): Item?{
        return itemDao?.getItem(id)
    }

    fun deleteAll() {
        itemDao?.deleteAll()
    }

    
}