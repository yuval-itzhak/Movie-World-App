package com.example.architectureproject.data.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import il.co.syntax.architectureprojects.Item

@Database(entities = arrayOf(Item::class), version = 1, exportSchema = false)
abstract class ItemDataBase : RoomDatabase() {
    abstract fun itemDao() : ItemDao

    companion object{
        @Volatile
        private var instance: ItemDataBase? = null

        fun getDataBase(context : Context) = instance ?: synchronized(this){
            Room.databaseBuilder(context.applicationContext, ItemDataBase::class.java, "items_db")
                .build().also { instance = it }
        }

    }

}