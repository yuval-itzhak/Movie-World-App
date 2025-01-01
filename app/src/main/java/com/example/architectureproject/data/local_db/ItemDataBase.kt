package com.example.architectureproject.data.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.architectureproject.data.model.Item

@Database(entities = [Item::class], version = 4, exportSchema = false)
abstract class ItemDataBase : RoomDatabase() {

    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var instance: ItemDataBase? = null

        // Migration from version 1 to 2
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE items ADD COLUMN genre TEXT")
            }
        }

        // Migration from version 2 to 3
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Example: Adding a new column, replace this with actual schema changes
                database.execSQL("ALTER TABLE items ADD COLUMN additionalField TEXT DEFAULT '' NOT NULL")
            }
        }
        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add the 'genre' column with a NOT NULL constraint and a default value of an empty string
                database.execSQL("ALTER TABLE items ADD COLUMN genre TEXT NOT NULL DEFAULT ''")
            }
        }

        fun getDataBase(context: Context): ItemDataBase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    ItemDataBase::class.java,
                    "items_db"
                )
                    .addMigrations(MIGRATION_3_4) // Add all migrations
                    .build().also { instance = it }
            }
        }
    }
}
