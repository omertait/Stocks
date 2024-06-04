package com.example.stocksapp.data.repositories

import com.example.stocksapp.data.local.ItemDao
import com.example.stocksapp.data.model.Item
import javax.inject.Inject


class ItemRepository @Inject constructor(private val itemDao: ItemDao) {

    fun getItems() = itemDao.getItems()

//    fun getFavorites() = itemDao.getFavorites()


    suspend fun addItem(item: Item) {
        itemDao.addItem(item)
    }


    suspend fun deleteItem(item: Item) {
        itemDao.deleteItem(item)
    }


    suspend fun deleteAll() {
        itemDao.deleteAll()
    }

    suspend fun updateItem(item: Item) {
        itemDao.updateItem(item)
    }

}