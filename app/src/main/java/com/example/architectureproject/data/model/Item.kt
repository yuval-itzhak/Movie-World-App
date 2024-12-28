package com.example.architectureproject.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


//@Parcelize
@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int  = 0,
    @ColumnInfo(name = "content")
    val title:String,
    @ColumnInfo(name = "director")
    val director: String,
    @ColumnInfo(name = "writer")
    val writer: String,
    @ColumnInfo(name = "stars")
    val stars: String,
    @ColumnInfo(name = "release")
    val release: Int,
    @ColumnInfo(name = "description")
    val description:String,
    @ColumnInfo(name = "image")
    val photo: String?
)
