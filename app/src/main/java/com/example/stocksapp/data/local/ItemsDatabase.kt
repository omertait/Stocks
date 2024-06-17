package com.example.stocksapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.stocksapp.data.model.Item


/**
 * The Room database for this app.
 * This database contains a single table (items_table) which is represented by the Item entity.
 */
@Database(entities = [Item::class], version = 2, exportSchema = false)
abstract class ItemsDatabase : RoomDatabase() {

    /**
     * Get the ItemDao for database operations.
     *
     * @return the ItemDao.
     */
    abstract fun itemsDao(): ItemDao

    companion object {
        // The @Volatile annotation ensures that the value of instance is always up-to-date and the same to all execution threads.
        @Volatile
        private var instance: ItemsDatabase? = null

        /**
         * Get the singleton instance of ItemsDatabase.
         * If the instance is null, create a new one. Otherwise, return the existing instance.
         *
         * @param context the application context.
         * @return the singleton instance of ItemsDatabase.
         */
        fun getDatabase(context: Context) = instance ?: synchronized(ItemsDatabase::class.java) {
            // Create the database builder for Room database with fallback strategy.
            Room.databaseBuilder(
                context.applicationContext,
                ItemsDatabase::class.java, "items_database"
            ).fallbackToDestructiveMigration().build().also { instance = it }
        }
    }
}