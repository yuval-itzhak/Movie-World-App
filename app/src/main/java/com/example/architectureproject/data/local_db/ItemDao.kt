package com.example.architectureproject.data.local_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.architectureproject.data.model.Item

@Dao
interface  ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addItem(item : Item)

    @Delete
    fun deleteItem(item : Item)

    @Update
    fun update(item: Item)

    @Query("SELECT *  FROM items ORDER BY content ASC")
    fun getItems() : LiveData<List<Item>>

    @Query("SELECT * FROM items WHERE content LIKE :id")
    fun getItem(id : Int): Item

    @Query ("DELETE FROM items")
    fun deleteAll()
}