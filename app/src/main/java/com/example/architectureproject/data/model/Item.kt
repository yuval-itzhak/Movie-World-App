package il.co.syntax.architectureprojects

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//import kotlinx.parcelize.Parcelize
import java.io.Serializable

//@Parcelize
@Entity(tableName = "items")
data class Item(
    @ColumnInfo(name = "content")
    val title:String,
    @ColumnInfo(name = "description")
    val description:String,
    @ColumnInfo(name = "image")
    val photo: String?){

        @PrimaryKey(autoGenerate = true)
        var id : Int = 0

    }




//object ItemManager {
//
//    val items : MutableList<Item> = mutableListOf()
//
//    fun add(item: Item) {
//        items.add(item)
//    }
//
//    fun remove(index:Int) {
//        items.removeAt(index)
//    }
//}