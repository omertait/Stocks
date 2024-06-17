package com.example.stocksapp.data.repositories

import com.example.stocksapp.data.local.ItemDao
import com.example.stocksapp.data.local.ItemsDatabase
import com.example.stocksapp.data.model.Item
import javax.inject.Inject
import android.app.Application


/**
 * Repository for handling operations related to the Item entity.
 *
 * @property itemDao The Data Access Object (DAO) for performing database operations.
 */
class ItemRepository @Inject constructor(private val itemDao: ItemDao) {

    /**
     * Retrieves all items from the database.
     *
     * @return A LiveData list of all items.
     */
    fun getItems() = itemDao.getItems()

    /**
     * Adds an item to the database.
     *
     * @param item The item to be added.
     */
    suspend fun addItem(item: Item) {
        itemDao.addItem(item)
    }

    /**
     * Deletes an item from the database.
     *
     * @param item The item to be deleted.
     */
    suspend fun deleteItem(item: Item) {
        itemDao.deleteItem(item)
    }

    /**
     * Deletes all items from the database.
     */
    suspend fun deleteAll() {
        itemDao.deleteAll()
    }

    /**
     * Updates an item in the database.
     *
     * @param item The item to be updated.
     */
    suspend fun updateItem(item: Item) {
        itemDao.updateItem(item)
    }
}