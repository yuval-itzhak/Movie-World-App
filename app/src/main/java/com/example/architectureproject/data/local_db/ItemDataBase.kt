package com.example.architectureproject.data.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.architectureproject.data.model.Item

@Database(entities = [Item::class], version = 2, exportSchema = false)
abstract class ItemDataBase : RoomDatabase() {
    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var instance: ItemDataBase? = null

        fun getDataBase(context: Context) = instance ?: synchronized(this) {
            Room.databaseBuilder(
                context.applicationContext,
                ItemDataBase::class.java,
                "items_db"
            )
                .fallbackToDestructiveMigration() // Allow destructive migration
                .build()
                .also { instance = it }
        }
    }
}
