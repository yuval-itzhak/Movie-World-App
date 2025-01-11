package com.example.movieworldproject.data.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.movieworldproject.data.model.Movie

@Database(entities = [Movie::class], version = 6, exportSchema = false)
abstract class MovieDataBase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile
        private var instance: MovieDataBase? = null

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
        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //rename database name
                database.execSQL("ALTER TABLE items ADD COLUMN genre TEXT NOT NULL DEFAULT ''")
            }
        }
        private val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE items ADD COLUMN video_id TEXT")
            }
        }

        fun getDataBase(context: Context): MovieDataBase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    MovieDataBase::class.java,
                    "movies_db"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6) //Add all migrations
                    .build().also { instance = it }
            }
        }
    }
}
