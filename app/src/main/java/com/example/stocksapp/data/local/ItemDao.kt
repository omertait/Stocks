package com.example.stocksapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.stocksapp.data.model.Item


@Dao
interface ItemDao {

    /**
     * Inserts an item into the items_table.
     * If the item already exists, it replaces it.
     *
     * @param item the item to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItem(item: Item)

    /**
     * Deletes one or more items from the items_table.
     *
     * @param item the items to be deleted.
     */
    @Delete
    suspend fun deleteItem(vararg item: Item)

    /**
     * Updates an existing item in the items_table.
     * If the item does not exist, it inserts it.
     *
     * @param item the item to be updated.
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateItem(item: Item)

    /**
     * Retrieves all items from the items_table ordered by stockSymbol in ascending order.
     *
     * @return a LiveData list of all items.
     */
    @Query("SELECT * from items_table ORDER BY stockSymbol ASC")
    fun getItems(): LiveData<List<Item>>

    /**
     * Retrieves an item from the items_table that matches the provided stockSymbol.
     *
     * @param title the stockSymbol of the item to be retrieved.
     * @return the item that matches the provided stockSymbol.
     */
    @Query("SELECT * from items_table WHERE stockSymbol LIKE :title")
    suspend fun getItem(title: String): Item

    /**
     * Deletes all items from the items_table.
     */
    @Query("DELETE from items_table")
    suspend fun deleteAll()
}
